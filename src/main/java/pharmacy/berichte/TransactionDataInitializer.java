package pharmacy.berichte;

import org.salespointframework.core.DataInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;

@Component  // Spring will pick this up as a bean
@Order(21)   // after your CatalogDataInitializer (which is @Order(20))
public class TransactionDataInitializer implements DataInitializer {

    private final TransactionRepository repo;

    public TransactionDataInitializer(TransactionRepository repo) {
        this.repo = repo;
    }

    @Override
    public void initialize() {
        if (repo.count() > 0) {
            return;  // already seeded
        }

        repo.save(new Transaction(LocalDate.now().minusDays(2), "INSURANCE",
            "Rx #1229 - TK", new BigDecimal("45.60")));
        repo.save(new Transaction(LocalDate.now().minusDays(1), "INSURANCE",
            "Rx #1230 - AOK", new BigDecimal("30.00")));
        repo.save(new Transaction(LocalDate.now().minusDays(1), "CASH",
            "Rx #1234 – Bar", new BigDecimal("12.50")));
        repo.save(new Transaction(LocalDate.now().minusDays(1), "CASH",
            "Rx #1235 – Bar", new BigDecimal("25.00")));
        repo.save(new Transaction(LocalDate.now(), "CASH",
            "Rx #1236 – Bar", new BigDecimal("8.75")));
    }
}
