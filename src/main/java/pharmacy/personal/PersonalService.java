package pharmacy.personal;

import java.util.UUID;

import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pharmacy.user.User;
import pharmacy.user.UserRepository;
import pharmacy.user.User.UserIdentifier;

@Service
@Transactional
public class PersonalService {
    private final UserRepository userRepository;
    private final UserAccountManagement userAccounts;

    public PersonalService(UserRepository userRepository, UserAccountManagement userAccounts) {
        this.userRepository = userRepository;
        this.userAccounts = userAccounts;
    }

    public void deleteById(UUID id) {
        try {
            UserIdentifier userId = User.UserIdentifier.of(id.toString());
            userRepository.deleteById(userId);
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Löschen des Benutzers: " + e.getMessage(), e);
        }
    }
}