package pharmacy.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.money.MonetaryAmount;

import org.salespointframework.order.OrderManagement;
import org.salespointframework.payment.Cash;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import pharmacy.lab.LabOrder;

@Service
public class ReportService {

    private final OrderManagement<LabOrder> orders;
    private final ReportEntryRepository entryRepo;

    public ReportService(OrderManagement<LabOrder> orders,
                         ReportEntryRepository entryRepo) {
        this.orders = orders;
        this.entryRepo = entryRepo;
    }

    public List<LabOrder> getCashOrders() {
        return toList().stream()
            .filter(o -> Cash.CASH.equals(o.getPaymentMethod()))
            .collect(Collectors.toList());
    }

    public List<LabOrder> getInsuranceOrders() {
        return toList().stream()
            .filter(o -> !Cash.CASH.equals(o.getPaymentMethod()))
            .collect(Collectors.toList());
    }

    public BigDecimal sumOrders(List<LabOrder> list) {
        return list.stream()
            .map(o -> toBigDecimal(o.getOrderLines().getTotal()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isRefunded(String id) {
        return entryRepo.findById(id)
                        .map(ReportEntry::isRefunded)
                        .orElse(false);
    }

    public void setRefunded(String id, boolean refunded) {
        ReportEntry entry = entryRepo.findById(id).orElse(new ReportEntry(id));
        entry.setRefunded(refunded);
        entryRepo.save(entry);
    }

    private List<LabOrder> toList() {
        return StreamSupport.stream(
                orders.findAll(Pageable.unpaged()).spliterator(), false)
            .collect(Collectors.toList());
    }

    private BigDecimal toBigDecimal(MonetaryAmount amt) {
        return amt.getNumber().numberValue(BigDecimal.class);
    }
}