package pharmacy.report.DataTransferObject;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReportOrder {

    private final String id;
    private final LocalDate date;
    private final BigDecimal amount;
    private final boolean paid;
    private final boolean refunded;

    public ReportOrder(String id,
                       LocalDate date,
                       BigDecimal amount,
                       boolean paid,
                       boolean refunded) {
        this.id       = id;
        this.date     = date;
        this.amount   = amount;
        this.paid     = paid;
        this.refunded = refunded;
    }

    public String getId()          { return id; }
    public LocalDate getDate()     { return date; }
    public BigDecimal getAmount()  { return amount; }
    public boolean isPaid()        { return paid; }
    public boolean isRefunded()    { return refunded; }
}
