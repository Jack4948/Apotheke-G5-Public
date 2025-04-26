package pharmacy.User;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

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
        
        // Wenn Abbrechen gedrückt wurde, zurück zur Startseite
        if ("cancel".equals(action)) {
            return "redirect:/";
        }
        //passwort pruefen
        if (!form.getPassword().equals(form.getPasswordConfirm())) {
            model.addAttribute("passwordMismatch", true);
            return "registrieren";
        }

        if (bindingResult.hasErrors()) {
            return "registrieren";
        }

        try {
            userService.createUser(form);
            // Erfolgsmeldung für Login-Seite
            redirectAttributes.addFlashAttribute("registrationSuccess", true);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Registrierung fehlgeschlagen: " + e.getMessage());
            return "registrieren";
        }
    }
    
    @GetMapping("/Kasse")
    public String kasse() {
        return "Kasse";
    }
}