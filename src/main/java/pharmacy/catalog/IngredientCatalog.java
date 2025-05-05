package pharmacy.catalog;

import org.salespointframework.catalog.Catalog;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;

public interface IngredientCatalog extends Catalog<MixtureIngredient> {

	static final Sort DEFAULT_SORT = Sort.sort(MixtureIngredient.class).by(MixtureIngredient::getName).ascending();

	/**
	 * Returns all {@link MixtureIngredient}s by type ordered by the given
	 * {@link Sort}.
	 *
	 * @param sort must not be {@literal null}.
	 * @return the ingredients of the given type, never {@literal null}.
	 */
	Streamable<MixtureIngredient> findAll(Sort sort);

	/**
	 * Returns all {@link MixtureIngredient}s by type ordered by their name.
	 *
	 * @return the ingredients of the given type, never {@literal null}.
	 */
	default Streamable<MixtureIngredient> findAll() {
		return findAll(DEFAULT_SORT);
	}
}
