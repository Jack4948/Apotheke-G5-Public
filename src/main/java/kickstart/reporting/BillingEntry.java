package kickstart.reporting;

public class BillingEntry {
    private String practiceName;       // Praxis Müller
    private String medicineName;        // Aspirin
    private int quantity;               // 2
    private double pricePerUnit;        // 5.50
    private String date;                // 27.04.2025

    public BillingEntry(String practiceName, String medicineName, int quantity, double pricePerUnit, String date) {
        this.practiceName = practiceName;
        this.medicineName = medicineName;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.date = date;
    }

    public String getPracticeName() {
        return practiceName;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public String getDate() {
        return date;
    }

    public double getTotalPrice() {
        return pricePerUnit * quantity;
    }
}
