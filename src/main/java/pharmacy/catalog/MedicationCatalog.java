package pharmacy.catalog;

import org.salespointframework.catalog.Catalog;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;

public interface MedicationCatalog extends Catalog<Medication> {

	static final Sort DEFAULT_SORT = Sort.sort(Medication.class).by(Medication::getName).ascending();

	/**
	 * Returns all {@link Medication}s by type ordered by the given {@link Sort}.
	 *
	 * @param sort must not be {@literal null}.
	 * @return the medications of the given type, never {@literal null}.
	 */
	Streamable<Medication> findAll(Sort sort);

	/**
	 * Returns all {@link Medication}s by type ordered by their identifier.
	 *
	 * @return the medications of the given type, never {@literal null}.
	 */
	default Streamable<Medication> findAll() {
		return findAll(DEFAULT_SORT);
	}
}
