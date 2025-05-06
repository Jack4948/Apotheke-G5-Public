package pharmacy.personal;

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

    // Registrierung normaler Benutzer
    public User createUser(RegistrationForm form) {
        Role role = Role.of(form.getRole());
        UserAccount userAccount = userAccounts.create(
                form.getFirstName(),
                Password.UnencryptedPassword.of(form.getPassword()),
                role
        );



        boolean isChef = false;
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            isChef = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(authority -> authority.equals("ROLE_BOSS"));
        } catch (Exception ignored) {
        }

        userAccount.setEnabled(isChef);
        userAccounts.save(userAccount);

        User user = new User(
                userAccount,
                form.getFirstName() + " " + form.getLastName(),
                form.getFirstName(),
                form.getPassword(),
                form.getRole(),
                isChef
        );


        return userRepository.save(user);
    }

    // Registrierung für Klinik
    public User createClinic(ClinicRegistrierenForm form) {
        Role role = Role.of("DOCTORS_OFFICE");

        UserAccount userAccount = userAccounts.create(
                form.getFirstName(),
                Password.UnencryptedPassword.of(form.getPassword()),
                role
        );

        userAccount.setEnabled(true);
        userAccounts.save(userAccount);

        User user = new User(
                userAccount,
                form.getFirstName() + " " + form.getLastName(),
                form.getFirstName(),
                form.getPassword(),
                "DOCTORS_OFFICE",
                true
        );


        return userRepository.save(user);
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

    // Benutzer löschen
    public void deleteById(UUID id) {
        try {
            UserIdentifier userId = User.UserIdentifier.of(id.toString());
            userRepository.deleteById(userId);
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Löschen des Benutzers: " + e.getMessage(), e);
        }
    }

    // ist Klinik schon registriert
    public boolean clinicExists() {
        return userRepository.findAll()
                .stream()
                .anyMatch(user -> "DOCTORS_OFFICE".equalsIgnoreCase(user.getRole()));
    }

    // erste gefundene Klinik zurückgeben
    public User getClinicIfExists() {
        var list = userRepository.findAll().toList();

        list.forEach(u -> System.out.println(u.getRole()));

        return list.stream()
                .filter(u -> u.getRole() != null && u.getRole().trim().equalsIgnoreCase("DOCTORS_OFFICE"))
                .findFirst()
                .orElse(null);
    }
}