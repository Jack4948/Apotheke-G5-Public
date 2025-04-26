package pharmacy.User;

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserAccountManagement userAccounts;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, 
                      UserAccountManagement userAccounts,
                      PasswordEncoder passwordEncoder) {
        
        this.userRepository = userRepository;
        this.userAccounts = userAccounts;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(RegistrierenForm form) {
        // Benutzer-Account erstellen mit firstName als Benutzername
        Role role = Role.of(form.getRolle());
        UserAccount userAccount = userAccounts.create(
            form.getFirstName(),  // Benutze firstName als Benutzername
            UnencryptedPassword.of(form.getPassword()),
            role
        );
        
        // User-Objekt speichern
        User user = new User(
            userAccount, 
            form.getFirstName() + " " + form.getLastName(), 
            form.getFirstName(),  // Benutze firstName als Username
            passwordEncoder.encode(form.getPassword()),
            form.getEmail(),
            UserRole.valueOf(form.getRolle()),
            true
        );
        
        return userRepository.save(user);
    }

    public Streamable<User> findAll() {
        return userRepository.findAll();
    }
}