package pharmacy.catalog;

import static org.salespointframework.core.Currencies.EURO;
import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.payment.Cash;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import pharmacy.lab.LabOrder;

/**
 * A {@link DataInitializer} implementation that will create dummy data for the
 * application on application startup.
 *
 * @see DataInitializer
 */
@Component
@Order(20)
class CatalogDataInitializer implements DataInitializer {

  private static final Logger LOG = LoggerFactory.getLogger(CatalogDataInitializer.class);

  private final MedicationCatalog medicationCatalog;
  private final IngredientCatalog ingredientCatalog;
  private final OrderManagement<LabOrder> labOrderManagement;
  private final UserAccountManagement userAccountManagement;
  private final UniqueInventory<UniqueInventoryItem> inventory;

  CatalogDataInitializer(MedicationCatalog medicationCatalog, IngredientCatalog ingredientCatalog,
      OrderManagement<LabOrder> labOrderManagement, UserAccountManagement userAccountManagement,
      UniqueInventory<UniqueInventoryItem> inventory) {

    Assert.notNull(medicationCatalog, "MedicationCatalog must not be null!");

    this.medicationCatalog = medicationCatalog;
    this.ingredientCatalog = ingredientCatalog;
    this.labOrderManagement = labOrderManagement;
    this.userAccountManagement = userAccountManagement;
    this.inventory = inventory;
  }

  @Override
  public void initialize() {
    UserAccount anonymous = userAccountManagement.create("anonymous", UnencryptedPassword.of("anonymous"));

    if (medicationCatalog.findAll().iterator().hasNext()) {
      return;
    }

    LOG.info("Creating default catalog entries.");

    Medication ibuprofen =  medicationCatalog.save(new Medication("1234567890", "Ibuprofen 400mg", Money.of(9.99, EURO), "20 Tabletten", true, 11));
    Medication paracetamol = medicationCatalog.save(new Medication("0987654321", "Paracetamol", Money.of(5.99, EURO), "30 Tabletten", false, 25));

    MixtureIngredient ingredient1 = ingredientCatalog.save(new MixtureIngredient("Lorem", Money.of(0.09, EURO), false));
    MixtureIngredient ingredient2 = ingredientCatalog.save(new MixtureIngredient("Ipsum", Money.of(0.05, EURO), true));

    inventory.save(new UniqueInventoryItem(ingredient1, Quantity.of(10000)));
    inventory.save(new UniqueInventoryItem(ingredient2, Quantity.of(10000)));

    LabOrder order1 = new LabOrder(anonymous.getId(), Cash.CASH);
    order1.addOrderLine(ingredient1, Quantity.of(20));
    order1.addOrderLine(ingredient2, Quantity.of(50));
    labOrderManagement.save(order1);
    labOrderManagement.payOrder(order1);

    LabOrder order2 = new LabOrder(anonymous.getId(), Cash.CASH);
    order2.addOrderLine(ingredient1, Quantity.of(10));       // 10g Lorem
    labOrderManagement.save(order2);
    labOrderManagement.payOrder(order2);
    labOrderManagement.completeOrder(order2);

    LabOrder cashPaid = new LabOrder(anonymous.getId(), Cash.CASH);
    cashPaid.addOrderLine(ingredient2, Quantity.of(5));
    labOrderManagement.save(cashPaid);
    labOrderManagement.payOrder(cashPaid);

    LabOrder cashUnpaid = new LabOrder(anonymous.getId(), Cash.CASH);
    cashUnpaid.addOrderLine(ingredient1, Quantity.of(15));
    labOrderManagement.save(cashUnpaid);

    LabOrder insPaid = new LabOrder(anonymous.getId());
    insPaid.addOrderLine(ingredient1, Quantity.of(8));
    labOrderManagement.save(insPaid);
    labOrderManagement.payOrder(insPaid);

    LabOrder insPaid2 = new LabOrder(anonymous.getId());
    insPaid2.addOrderLine(ingredient1, Quantity.of(50));
    labOrderManagement.save(insPaid2);
    labOrderManagement.payOrder(insPaid2);

    LabOrder medOrder1 = new LabOrder(anonymous.getId(), Cash.CASH);
    medOrder1.addOrderLine(ibuprofen, Quantity.of(5));
    labOrderManagement.save(medOrder1);
    labOrderManagement.payOrder(medOrder1);

    LabOrder medOrder2 = new LabOrder(anonymous.getId());
    medOrder2.addOrderLine(paracetamol, Quantity.of(3));
    labOrderManagement.save(medOrder2);
    //   LabOrder insPaid3 = new LabOrder(anonymous.getId());
    // insPaid3.addOrderLine(ingredient2, Quantity.of(20));
    // labOrderManagement.save(insPaid3);
    // labOrderManagement.payOrder(insPaid3);

  }

}
