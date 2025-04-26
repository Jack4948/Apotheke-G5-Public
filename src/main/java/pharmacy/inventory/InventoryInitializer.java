package pharmacy.inventory;



import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import pharmacy.catalog.MedicationCatalog;

@Component // auto create instance at start
@Order(30) // set execution order
public class InventoryInitializer implements DataInitializer {

	private final UniqueInventory<UniqueInventoryItem> inventory;
	private final MedicationCatalog medicationCatalog;

	InventoryInitializer(UniqueInventory<UniqueInventoryItem> inventory, MedicationCatalog medicationCatalog) {
		Assert.notNull(inventory, "Inventory must not be null.");
		Assert.notNull(medicationCatalog, "MedicationCatalog must not be null.");

		this.inventory = inventory;
		this.medicationCatalog = medicationCatalog;
	}

	@Override
	public void initialize() {
		medicationCatalog.findAll().forEach(medication -> {
			if (inventory.findByProduct(medication).isEmpty()) {
				inventory.save(new UniqueInventoryItem(medication, Quantity.of(10)));
			}
		});
	}
}
