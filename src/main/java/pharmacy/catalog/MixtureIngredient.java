package pharmacy.catalog;

import jakarta.persistence.Entity;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

@Entity
public class MixtureIngredient extends Product {
	private boolean needsPrescription;

	@SuppressWarnings({ "unused", "deprecation" })
	private MixtureIngredient() {}

	public MixtureIngredient(String name, Money price, boolean needsPrescription) {

		super(name, price);
		this.needsPrescription = needsPrescription;
	}

	public boolean getNeedsPrescription() {
		return this.needsPrescription;
	}
}
