package pharmacy.report;

import java.math.BigDecimal;
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

    public ReportService(OrderManagement<LabOrder> orders) {
        this.orders = orders;
    }

    /** All lab orders paid in cash. */
    public List<LabOrder> getCashOrders() {
        return toList().stream()
            .filter(o -> Cash.CASH.equals(o.getPaymentMethod()))
            .filter(LabOrder::isPaid)
            .collect(Collectors.toList());
    }

    /** All lab orders paid by insurance. */
    public List<LabOrder> getInsuranceOrders() {
        return toList().stream()
            .filter(o -> !Cash.CASH.equals(o.getPaymentMethod()))
            .collect(Collectors.toList());
    }

    /** Total sum of cash‐paid lab orders. */
    public BigDecimal getCashTotal() {
        return getCashOrders().stream()
                .map(o -> toBigDecimal(o.getOrderLines().getTotal()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** Total sum of insurance‐paid lab orders. */
    public BigDecimal getInsuranceTotal() {
        return getInsuranceOrders().stream()
                .map(o -> toBigDecimal(o.getOrderLines().getTotal()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // helpers:

    private List<LabOrder> toList() {
        return StreamSupport
            .stream(orders.findAll(Pageable.unpaged()).spliterator(), false)
            .collect(Collectors.toList());
    }

    private BigDecimal toBigDecimal(MonetaryAmount amt) {
        return amt.getNumber().numberValue(BigDecimal.class);
    }
}
