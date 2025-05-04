package pharmacy.User;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.UUID;

@Controller
public class UserController {
  
    private final UserService userService;

    public UserController(UserService userService) {
        Assert.notNull(userService, "UserService darf nicht null sein!");
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registrieren")
    public String registerForm(Model model) {
        if (!model.containsAttribute("registrierenForm")) {
            model.addAttribute("registrierenForm", new RegistrierenForm(null, null, null, null, null, null));
        }
        return "registrieren";
    }

    @PostMapping("/registrieren")
    public String registerPost(@Valid @ModelAttribute("registrierenForm") RegistrierenForm form,
                              BindingResult bindingResult,
                              @RequestParam(name = "action", defaultValue = "") String action,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        
        if ("cancel".equals(action)) {
            return "redirect:/";
        }
        
        if (!form.getPassword().equals(form.getPasswordConfirm())) {
            model.addAttribute("passwordMismatch", true);
            return "registrieren";
        }

        if (bindingResult.hasErrors()) {
            return "registrieren";
        }

        try {
            userService.createUser(form);
            redirectAttributes.addFlashAttribute("registrationSuccess", true);
            if (form.getIsFromPersonal()) {
                redirectAttributes.addFlashAttribute("successMessage", "Benutzer wurde erfolgreich erstellt");
                return "redirect:/admin/benutzer";
            }
            else
                return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Registrierung fehlgeschlagen: " + e.getMessage());
            return "registrieren";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    // Personalverwaltung :

    @GetMapping("/admin/benutzer")
    public String personal(Model model){
        model.addAttribute("users", userService.findAll());
        return "/admin/benutzer";
    }

    @GetMapping("/admin/benutzer/add")
    public String addEmployee(Model model) {
        RegistrierenForm form = new RegistrierenForm(null, null, null, null, null, null);
        form.setIsFromPersonal(true);
        model.addAttribute("registrierenForm", form);
        return "registrieren";
    }

    @PostMapping("/admin/benutzer/loeschen/{id}")
    public String deleteUser(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
            userService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Mitarbeiter erfolgreich gelöscht");
        
        return "redirect:/admin/benutzer";
    }
}