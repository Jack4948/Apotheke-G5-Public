package pharmacy.personal;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pharmacy.user.RegistrationForm;
import pharmacy.user.UserService;

@Controller
public class PesonalController {
    
    private final UserService userService;
    private final PersonalService personalService;

    public PesonalController(UserService userService, PersonalService personalService) {
        Assert.notNull(userService, "UserService darf nicht null sein!");
        Assert.notNull(personalService, "UserService darf nicht null sein!");
        this.userService = userService;
        this.personalService = personalService;
    }

    @GetMapping("/admin/benutzer")
    public String personal(Model model){
        model.addAttribute("users", userService.findAll());
        return "admin/benutzer";
    }

    @GetMapping("/admin/benutzer/hinzufuegen")
    public String addEmployee(Model model) {
        RegistrationForm form = new RegistrationForm(null, null, null, null, null);
        form.setIsFromPersonal(true);
        model.addAttribute("registrierenForm", form);
        return "registrieren";
    }

    @PostMapping("/admin/benutzer/loeschen/{id}")
    public String deleteUser(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
            personalService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Mitarbeiter erfolgreich gelöscht");
        
        return "redirect:/admin/benutzer";
    }
}