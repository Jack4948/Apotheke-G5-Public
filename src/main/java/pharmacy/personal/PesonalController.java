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
	private final PersonalService personalService;

	public PesonalController(UserService userService, PersonalService personalService) {
		Assert.notNull(userService, "UserService darf nicht null sein!");
		Assert.notNull(personalService, "PersonalService darf nicht null sein!");
		this.userService = userService;
		this.personalService = personalService;
	}

	@GetMapping("/admin/benutzer")
	public String personal(Model model) {

		model.addAttribute("users", userService.findAll());
	//	model.addAttribute("Docktor", personalService.getClinicIfExists());
		boolean anotherActiveDoctorOffice = personalService.isAnotherDoctorOfficeActive();
		model.addAttribute("isAnotherDoctorOfficeActive", anotherActiveDoctorOffice);


		return "admin/benutzer";
	}

	@GetMapping("/admin/benutzer/add")
	public String addEmployee(Model model) {
		RegistrationForm form = new RegistrationForm(null, null, null, null, null);
		form.setIsFromPersonal(true);
		model.addAttribute("registrierenForm", form);
		return "registrieren";
	}



	@PostMapping("/benutzer/loeschen/{id}")
	public String deleteUser(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
		try {
			personalService.deleteById(id);
			redirectAttributes.addFlashAttribute("successMessage", "Benutzer erfolgreich gelöscht");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Fehler beim Löschen des Benutzers: " + e.getMessage());
		}
		return "redirect:/admin/benutzer";
	}

	@PostMapping("/admin/benutzer/freischalten/{id}")
	public String enableUser(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
		personalService.enableUser(id);
		redirectAttributes.addFlashAttribute("successMessage", "Benutzer wurde freigeschaltet.");

		return "redirect:/admin/benutzer";
	}



}
