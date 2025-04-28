package pharmacy.berichte;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportController {

    private final TransactionMethod reportService;

    public ReportController(TransactionMethod reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/berichte/kassenabrechnung")
    public String kassenAbrechnung(Model model) {
        model.addAttribute("cashTx",      reportService.getCashTransactions());
        model.addAttribute("insuranceTx", reportService.getInsuranceTransactions());

        model.addAttribute("cashTotal",      reportService.getCashTotal());
        model.addAttribute("insuranceTotal", reportService.getInsuranceTotal());
        return "berichte";
    }
}
