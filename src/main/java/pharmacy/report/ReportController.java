//Controller
package pharmacy.report;

//import pharmacy.report.ReportService;
//import pharmacy.order.LabOrder; 

//import org.salespointframework.order.OrderManagement;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal; 
import java.util.List;
import java.util.stream.Collectors;

import pharmacy.lab.LabOrder;

@Controller
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/report")
    public String report(
        @RequestParam(name = "type", defaultValue = "ALL") String type,
        @RequestParam(name = "status", defaultValue = "ALL") String status,
        Model model) {
        
        List<LabOrder> cash = reportService.getCashOrders();
        List<LabOrder> insurance = reportService.getInsuranceOrders();
        
        if (!"ALL".equals(status)) {
            boolean wantPaid = "PAID".equals(status);
            cash      = cash.stream()
                            .filter(o -> o.isPaid() == wantPaid)
                            .collect(Collectors.toList());
            insurance = insurance.stream()
                            .filter(o -> o.isPaid() == wantPaid)
                            .collect(Collectors.toList());
        }

        BigDecimal cashTotalFiltered      = reportService.sumOrders(cash);
        BigDecimal insuranceTotalFiltered = reportService.sumOrders(insurance);

        model.addAttribute("filterType",      type);
        model.addAttribute("filterStatus", status);  
        model.addAttribute("cashOrders",      cash);
        model.addAttribute("insuranceOrders", insurance);
        // model.addAttribute("cashTotal",       reportService.getCashTotal());
        // model.addAttribute("insuranceTotal",  reportService.getInsuranceTotal());
        model.addAttribute("cashTotal",       cashTotalFiltered);
        model.addAttribute("insuranceTotal",  insuranceTotalFiltered);


        return "report";
    }
}


