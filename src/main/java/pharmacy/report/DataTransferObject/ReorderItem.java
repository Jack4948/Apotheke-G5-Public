// src/main/java/pharmacy/report/ReorderItem.java
package pharmacy.report.DataTransferObject;

public class ReorderItem {

    private final String id;
    private final String name;
    private final int currentStock;
    private final int reorderThreshold;

    public ReorderItem(String id, String name, int currentStock, int reorderThreshold) {
        this.id               = id;
        this.name             = name;
        this.currentStock     = currentStock;
        this.reorderThreshold = reorderThreshold;
    }

    public String getId()               { return id; }
    public String getName()             { return name; }
    public int    getCurrentStock()     { return currentStock; }
    public int    getReorderThreshold() { return reorderThreshold; }
}
