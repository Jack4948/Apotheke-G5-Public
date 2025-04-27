package kickstart.reporting;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class BillingController {

    @GetMapping("/billing")
    public String showBillingReport(Model model) {

        List<BillingEntry> entries = List.of(
            new BillingEntry("Praxis Müller", "Aspirin", 2, 5.50, "27.04.2025"),
            new BillingEntry("Praxis Schmidt", "Ibuprofen", 1, 8.20, "26.04.2025"),
            new BillingEntry("Praxis Maier", "Paracetamol", 3, 4.75, "25.04.2025")
        );

        model.addAttribute("entries", entries);
        return "billing";
    }
}
