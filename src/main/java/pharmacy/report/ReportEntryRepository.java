package pharmacy.report;

import org.springframework.data.repository.CrudRepository;

 
public interface ReportEntryRepository extends CrudRepository<ReportEntry, Long> {
    // This repository is used to store the refund status of orders.
}
