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
     private String id;

    private boolean refunded = false;

    protected ReportEntry() { }

    public ReportEntry(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean isRefunded() {
        return refunded;
    }

    public void setRefunded(boolean refunded) {
        this.refunded = refunded;
    }
}