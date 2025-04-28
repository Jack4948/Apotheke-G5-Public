package pharmacy.order;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class LabController {
  @GetMapping("/lab-list")
  public String listLabs(Model model) {
    // …
    return "lab-list";
  }
}
