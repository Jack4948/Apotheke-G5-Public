package pharmacy.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.money.MonetaryAmount;

import org.salespointframework.order.OrderManagement;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.payment.Cash;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import pharmacy.lab.LabOrder;
import pharmacy.report.DataTransferObject.ReorderItem;
import pharmacy.catalog.Medication;

@Service
public class ReportService {

    private final OrderManagement<LabOrder> orders;
    private final ReportEntryRepository entryRepo;
    private final UniqueInventory<UniqueInventoryItem> inventory;

    public ReportService(OrderManagement<LabOrder> orders,
                         ReportEntryRepository entryRepo,
                         UniqueInventory<UniqueInventoryItem> inventory) {
        this.orders = orders;
        this.entryRepo = entryRepo;
        this.inventory = inventory;
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

   public List<ReorderItem> findToReorder() {
    return StreamSupport.stream(inventory.findAll().spliterator(), false)
        // 1) only keep those whose product is a Medication
        .filter(item -> item.getProduct() instanceof Medication)
        // 2) map to our DTO
        .map(item -> {
            Medication med = (Medication) item.getProduct();
            int stock = item.getQuantity()
                            .getAmount()
                            .intValue();
            return new ReorderItem(
                med.getBarcode(),
                med.getName(),
                stock,
                med.getReorderThreshold()
            );
        })
        // 3) only those at-or-below threshold
        .filter(ri -> ri.getCurrentStock() <= ri.getReorderThreshold())
        .collect(Collectors.toList());
}

}