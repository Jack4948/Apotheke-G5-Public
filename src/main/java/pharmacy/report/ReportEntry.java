package pharmacy.report;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA entity to track refunded (erstattet) status per LabOrder ID.
 */
@Entity
@Table(name = "report_entry")
public class ReportEntry {

    @Id
    private Long id;                // matches LabOrder.getId()

    private boolean refunded = false;

    protected ReportEntry() { }

    public ReportEntry(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public boolean isRefunded() {
        return refunded;
    }

    public void setRefunded(boolean refunded) {
        this.refunded = refunded;
    }
}