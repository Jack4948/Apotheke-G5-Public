package pharmacy.personal;

import java.util.List;
import java.util.UUID;

import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pharmacy.user.RegistrationForm;
import pharmacy.user.User;
import pharmacy.user.User.UserIdentifier;
import pharmacy.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;

@Service
@Transactional
public class PersonalService {

	private final UserRepository userRepository;
	private final UserAccountManagement userAccounts;

	public PersonalService(UserRepository userRepository, UserAccountManagement userAccounts) {
		this.userRepository = userRepository;
		this.userAccounts = userAccounts;
	}

	//aktiviert inizialuser
	public void activateInitialUsers() {

		List<String> initialRoles = List.of("BOSS", "EMPLOYEE", "DELIVERY_DRIVER");

		userRepository.findAll().stream()
			.filter(user -> initialRoles.contains(user.getRole()))
			.forEach(user -> {
				System.out.println("Aktiviere Benutzer mit Rolle: " + user.getRole());
				user.setEnabled(true);
				user.getUserAccount().setEnabled(true);
				userAccounts.save(user.getUserAccount());
				userRepository.save(user);
			});
	}


	private boolean isInitialUser(String role) {
		List<String> initialRoles = List.of("BOSS", "EMPLOYEE", "DELIVERY_DRIVER");
		return initialRoles.contains(role);
	}

	// Benutzer aktivieren
	public void enableUser(UUID id) {
		UserIdentifier userId = User.UserIdentifier.of(id);
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("Benutzer nicht gefunden"));
		user.setEnabled(true);
		user.getUserAccount().setEnabled(true);
		userAccounts.save(user.getUserAccount());
		userRepository.save(user);
	}


	public boolean isAnotherDoctorOfficeActive() {
		return userRepository.findAll().stream()
			.anyMatch(user -> "DOCTORS_OFFICE".equalsIgnoreCase(user.getRole()) && user.getEnabled());
	}

	// löschen
	public void deleteById(UUID id) {
		try {
			UserIdentifier userId = User.UserIdentifier.of(id.toString());
			User user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("Benutzer nicht gefunden"));

			//  UserAccount deaktivieren
			UserAccount userAccount = user.getUserAccount();
			if (userAccount != null) {
				userAccount.setEnabled(false);
				userAccounts.save(userAccount);
			}

			//  User löschen
			userRepository.deleteById(userId);


		} catch (Exception e) {
			throw new RuntimeException("Fehler beim Löschen des Benutzers: " + e.getMessage(), e);
		}
	}


}