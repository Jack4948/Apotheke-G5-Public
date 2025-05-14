package pharmacy.catalog;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

@Entity
public class Medication extends Product {
	private String barcode;
	private String packaging;
	private boolean needsPrescription;
	private int reorderThreshold;

	@SuppressWarnings({ "unused", "deprecation" })
	private Medication() {}

	public Medication(String barcode, String name, Money price, String packaging, boolean needsPrescription, int reorderThreshold) {

		super(name, price);
		this.barcode = barcode;
		this.packaging = packaging;
		this.needsPrescription = needsPrescription;
		this.reorderThreshold = reorderThreshold;
	}

	public String getBarcode() {
		return this.barcode;
	}

	public String getPackaging() {
		return this.packaging;
	}

	public boolean getNeedsPrescription() {
		return this.needsPrescription;
	}

	public int getReorderThreshold() {
		return this.reorderThreshold;
	}
}
