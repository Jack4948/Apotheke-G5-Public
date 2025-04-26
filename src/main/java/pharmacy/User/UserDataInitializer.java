package pharmacy.User;

import java.util.List;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.UserAccountManagement;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.commons.lang.Assert;

public class UserDataInitializer implements DataInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(UserDataInitializer.class);

    private final UserAccountManagement userAccountManagement;
    private final UserService userService;

    public UserDataInitializer(UserAccountManagement userAccountManagement, UserService userService) {
        Assert.notNull(userAccountManagement, "UserAccount muss nicht null sein");
        Assert.notNull(userService, "UserService muss nicht null sein");
        
        this.userAccountManagement = userAccountManagement;
        this.userService = userService;
    }

    @Override
    public void initialize() {
        if (userAccountManagement.findByUsername("boss").isPresent()) {
            return;
        }

        LOG.info("Erstelle Standard-Benutzer");
        
        // Erstelle den Boss-Benutzer
        userAccountManagement.create("boss", UnencryptedPassword.of("123"), Role.of("BOSS"));

        var password = "321";

        // Erstelle Test-Benutzer
        List.of(
            new RegistrierenForm("test", "test", "test@gmail.com", "EMPLOYEE", password, password),
            new RegistrierenForm("test1", "tester", "test1@gmail.com", "EMPLOYEE", password, password)
        ).forEach(userService::createUser);
    }
    
}
