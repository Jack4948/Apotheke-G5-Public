package pharmacy.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pharmacy.lab.LabOrder;
import pharmacy.report.DataTransferObject.ReorderItem;
import pharmacy.report.DataTransferObject.ReportOrder;

@Controller
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/berichte/kassenabrechnung")
    public String report(
        @RequestParam(name = "type",   defaultValue = "ALL") String type,
        @RequestParam(name = "status", defaultValue = "ALL") String status,
        Model model) {

        //  Fetch raw LabOrder lists
        List<LabOrder> labCash      = reportService.getCashOrders();
        List<LabOrder> labInsurance = reportService.getInsuranceOrders();

        //  Apply paid/unpaid filter
        if (!"ALL".equals(status)) {
            boolean wantPaid = "PAID".equals(status);
            labCash      = labCash.stream()
                                  .filter(o -> o.isPaid() == wantPaid)
                                  .collect(Collectors.toList());
            labInsurance = labInsurance.stream()
                                  .filter(o -> o.isPaid() == wantPaid)
                                  .collect(Collectors.toList());
        }

        //  Map each LabOrder to a ReportOrder DTO
        List<ReportOrder> cashVM = labCash.stream()
            .map(this::toReportOrder)
            .collect(Collectors.toList());

        List<ReportOrder> insVM = labInsurance.stream()
            .map(this::toReportOrder)
            .collect(Collectors.toList());

        //  Compute totals over the DTO lists
        BigDecimal cashTotal = cashVM.stream() // view model 
            .map(ReportOrder::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal insuranceTotal = insVM.stream()
            .map(ReportOrder::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Expose everything to Thymeleaf
        model.addAttribute("filterType",       type);
        model.addAttribute("filterStatus",     status);
        model.addAttribute("cashOrders",       cashVM);
        model.addAttribute("insuranceOrders",  insVM);
        model.addAttribute("cashTotal",        cashTotal);
        model.addAttribute("insuranceTotal",   insuranceTotal);

        return "report";
    }

    // Helper: convert a LabOrder into our simple DTO for the view 
   private ReportOrder toReportOrder(LabOrder o) {
    String   id       = o.getId().toString();
    LocalDate date    = o.getDateCreated().toLocalDate();
    BigDecimal amount = o.getOrderLines()
                        .getTotal()
                        .getNumber()
                        .numberValue(BigDecimal.class);
    boolean  paid     = o.isPaid();
    boolean  refunded = reportService.isRefunded(id);

    return new ReportOrder(id, date, amount, paid, refunded);
    }


    // @PostMapping("/report/{id}/refund")
    // public String toggleRefund(
    //     @PathVariable("id") String id,
    //     @RequestParam("value") boolean refunded) {

    //     reportService.setRefunded(id, refunded);
        
    //     return "redirect:/report";
    // }

   @GetMapping("/berichte/nachbestellung")
    public String showReorderList(Model model) {
        List<ReorderItem> list = reportService.findToReorder();
        model.addAttribute("reorderList", list);
        return "reorder";
    }

    

  
}


