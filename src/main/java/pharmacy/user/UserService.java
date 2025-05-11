package pharmacy.user;

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.springframework.data.util.Streamable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pharmacy.user.User.UserIdentifier;

@Service
@Transactional
public class UserService {

	private final UserRepository userRepository;
	private final UserAccountManagement userAccounts;

	public UserService(UserRepository userRepository, UserAccountManagement userAccounts) {
		this.userRepository = userRepository;
		this.userAccounts = userAccounts;
	}

	public User createUser(RegistrationForm form) {
		Role role = Role.of(form.getRole());
		UserAccount userAccount = userAccounts.create(
			form.getFirstName(),  // Benutze firstName als Benutzername
			UnencryptedPassword.of(form.getPassword()),
			role
		);

		// Standard nicht aktiviert
		boolean isEnabled = false;


		userAccount.setEnabled(isEnabled);
		userAccounts.save(userAccount);

		User user = new User(
			userAccount,
			form.getFirstName() + " " + form.getLastName(),
			form.getFirstName(),
			form.getPassword(),
			form.getRole(),
			isEnabled

		);

		return userRepository.save(user);
	}

	public Streamable<User> findAll() {
		return userRepository.findAll();
	}
}