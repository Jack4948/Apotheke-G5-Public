package pharmacy.personal;

import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pharmacy.user.RegistrationForm;
import pharmacy.user.UserService;

@Controller
public class PesonalController {

    private final UserService userService;
    private final PersonalService per;
    private final PersonalService personalService;

    public PesonalController(UserService userService, PersonalService per,PersonalService personalService) {
        Assert.notNull(userService, "UserService darf nicht null sein!");
        Assert.notNull(per, "UserService darf nicht null sein!");
        Assert.notNull(personalService, "UserService darf nicht null sein!");
        this.userService = userService;
        this.per = per;
        this.personalService = personalService;
    }

    @GetMapping("/admin/benutzer")
    public String personal(Model model){
        model.addAttribute("users", userService.findAll());
        model.addAttribute("clinicUser", personalService.getClinicIfExists());
        return "admin/benutzer";
    }

    @GetMapping("/admin/benutzer/add")
    public String addEmployee(Model model) {
        RegistrationForm form = new RegistrationForm(null, null, null, null, null);
        form.setRole("DOCTORS_OFFICE");
        form.setIsFromPersonal(true);
        model.addAttribute("registrierenForm", form);
        return "registrieren";
    }

    @PostMapping("/benutzer/loeschen/{id}")
    public String deleteUser(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        per.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Mitarbeiter erfolgreich gelöscht");

        return "redirect:/benutzer";
    }
    @PostMapping("/admin/benutzer/freischalten/{id}")
    public String freischalten(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        per.enableUser(id);
        redirectAttributes.addFlashAttribute("successMessage", "Benutzer wurde freigeschaltet.");
        return "redirect:/admin/benutzer";
    }
// clinic verwaltung

    @GetMapping("/admin/clinic/add")
    public String addClinic(Model model) {
        ClinicRegistrierenForm form = new ClinicRegistrierenForm("", "", "", "", "");
        model.addAttribute("clinicRegistrierenForm", form);
        return "admin/clinic-registrieren";
    }

    @PostMapping("/admin/clinic/add")
    public String registerClinic(@Valid @ModelAttribute("clinicRegistrierenForm") ClinicRegistrierenForm form,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        if (per.clinicExists()) {
            model.addAttribute("errorMessage", "Es gibt bereits eine registrierte Klinik.");
            return "admin/clinic-registrieren";
        }

        if (!form.getPassword().equals(form.getPasswordConfirm())) {
            model.addAttribute("passwordMismatch", true);
            return "admin/clinic-registrieren";
        }

        if (bindingResult.hasErrors()) {
            return "admin/clinic-registrieren";
        }

        try {
            form.setRole("DOCTORS_OFFICE");
            per.createClinic(form); // Die neue Methode
            redirectAttributes.addFlashAttribute("successMessage", "Klinik erfolgreich registriert.");
            return "redirect:/admin/benutzer";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Fehler bei der Registrierung: " + e.getMessage());
            return "admin/clinic-registrieren";
        }
    }

    @PostMapping("/admin/clinic/loeschen/{id}")
    public String deleteClinic(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        per.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Klinik erfolgreich gelöscht.");
        return "redirect:/admin/benutzer";
    }
    // das soll ich darüber denken so bleibt oder in benuzer besser
    // @GetMapping("/admin/praxisverwaltung")/daschbord
    // public String showPraxisVerwaltung(Model model) {
    //  var allUsers = userService.findAll();
    // var clinic = allUsers.stream()
    //    .filter(user ->"DOCTORS_OFFICE".equalsIgnoreCase(user.getRole()))
    //  .findFirst()
    //   .orElse(null);

    // model.addAttribute("clinicUser", clinic);
    // return "admin/praxisverwaltung";
    //  }





}