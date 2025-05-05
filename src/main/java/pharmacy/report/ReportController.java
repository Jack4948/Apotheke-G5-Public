package pharmacy.report;

//import pharmacy.report.ReportService;
//import pharmacy.order.LabOrder; 

//import org.salespointframework.order.OrderManagement;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/report")
    public String report(Model model) {
        // Lab orders:
        model.addAttribute("cashOrders",      reportService.getCashOrders());
        model.addAttribute("insuranceOrders", reportService.getInsuranceOrders());
        model.addAttribute("cashTotal",       reportService.getCashTotal());
        model.addAttribute("insuranceTotal",  reportService.getInsuranceTotal());

        // (You can inject TransactionMethod the same way for “normal” orders)
        return "report";
    }
}


