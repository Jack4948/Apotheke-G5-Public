package pharmacy.berichte;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionMethod {
    private final TransactionRepository repo;

    public TransactionMethod(TransactionRepository repo) {
        this.repo = repo;
    }

    public List<Transaction> getCashTransactions() {
        return repo.findByType("CASH");
    }

    public List<Transaction> getInsuranceTransactions() {
        return repo.findByType("INSURANCE");
    }

      public BigDecimal getCashTotal() {
        return getCashTransactions().stream()
            .map(Transaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getInsuranceTotal() {
        return getInsuranceTransactions().stream()
            .map(Transaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
