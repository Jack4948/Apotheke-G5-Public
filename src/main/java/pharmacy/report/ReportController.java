package pharmacy.report;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

        List<LabOrder> cash      = reportService.getCashOrders();
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

        BigDecimal cashTotal      = reportService.sumOrders(cash);
        BigDecimal insuranceTotal = reportService.sumOrders(insurance);

        model.addAttribute("filterType",    type);
        model.addAttribute("filterStatus",  status);
        model.addAttribute("cashOrders",    cash);
        model.addAttribute("insuranceOrders", insurance);
        model.addAttribute("cashTotal",     cashTotal);
        model.addAttribute("insuranceTotal", insuranceTotal);

        return "report";
    }

    @PostMapping("/report/{id}/refund")
    public String toggleRefund(
        @PathVariable Long id,
        @RequestParam("value") boolean refunded) {

        reportService.setRefunded(id, refunded);
        return "redirect:/report";
    }
}