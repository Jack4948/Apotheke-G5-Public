package pharmacy.report;

//import pharmacy.report.ReportService;
//import pharmacy.order.LabOrder; 

//import org.salespointframework.order.OrderManagement;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import pharmacy.lab.LabOrder;

@Controller
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/report")
    public String report(
        @RequestParam(name = "type", defaultValue = "All") String type,
        Model model) {

        List<LabOrder> cash = reportService.getCashOrders();
        List<LabOrder> insurance = reportService.getInsuranceOrders();
        
        model.addAttribute("filterType",      type);
        model.addAttribute("cashOrders",      cash);
        model.addAttribute("insuranceOrders", insurance);
        model.addAttribute("cashTotal",       reportService.getCashTotal());
        model.addAttribute("insuranceTotal",  reportService.getInsuranceTotal());

        return "report";
    }
}


