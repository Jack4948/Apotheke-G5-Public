package pharmacy.catalog;

import static org.salespointframework.core.Currencies.EURO;
import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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

  CatalogDataInitializer(MedicationCatalog medicationCatalog) {

    Assert.notNull(medicationCatalog, "MedicationCatalog must not be null!");

    this.medicationCatalog = medicationCatalog;
  }

  @Override
  public void initialize() {

    if (medicationCatalog.findAll().iterator().hasNext()) {
      return;
    }

    LOG.info("Creating default catalog entries.");

    medicationCatalog.save(new Medication("1234567890", "Ibuprofen 400mg", Money.of(9.99, EURO), "20 Tabletten", true));
    medicationCatalog.save(new Medication("0987654321", "Paracetamol", Money.of(5.99, EURO), "30 Tabletten", false));

    for (var medication: medicationCatalog.findAll()) {
      System.out.println(medication);
    }
  }
}
