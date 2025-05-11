package pharmacy.user;

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
import pharmacy.user.UserService;

import java.util.UUID;

@Controller
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		Assert.notNull(userService, "UserService darf nicht null sein!");
		this.userService = userService;
	}

	@GetMapping("/")
	public String login() {
		return "login";
	}

	@GetMapping("/registrieren")
	public String registerForm(Model model) {
		if (!model.containsAttribute("registrierenForm")) {
			model.addAttribute("registrierenForm", new RegistrationForm(null, null, null, null, null));
		}
		return "registrieren";
	}

	@PostMapping("/registrieren")
	public String registerPost(@Valid @ModelAttribute("registrierenForm") RegistrationForm form,
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


			} else
				return "redirect:/admin/benutzer";

		} catch (Exception e) {
			model.addAttribute("errorMessage", "Registrierung fehlgeschlagen: " + e.getMessage());
			return "registrieren";
		}
	}

	@GetMapping("/dashboard")
	public String dashboard() {
		return "dashboard";
	}

}