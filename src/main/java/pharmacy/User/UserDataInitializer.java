package pharmacy.User;

import java.util.List;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.UserAccountManagement;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Order(10)
public class UserDataInitializer implements DataInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(UserDataInitializer.class);

    private final UserAccountManagement userAccountManagement;
    private final UserService userService;
    //private final UserRepository userRepository;
    //private final PasswordEncoder passwordEncoder;
   // private final RegistrierenForm registrierenForm;

    public UserDataInitializer(UserAccountManagement userAccountManagement, UserService userService) {
        Assert.notNull(userAccountManagement, "UserAccount muss nicht null sein");
        Assert.notNull(userService, "UserService muss nicht null sein");
        
        this.userAccountManagement = userAccountManagement;
        this.userService = userService;
        
    }

    @Override
    public void initialize() {

        if (userAccountManagement.findAll().iterator().hasNext()) {
			return;
		}

        LOG.info("Erstelle Standard-Benutzer");
        
        RegistrierenForm Chef = new RegistrierenForm("java", "java", "java@aposys.de","BOSS", "123", "123");
        RegistrierenForm employee = new RegistrierenForm("emp", "emp", "emp@aposys.de","EMPLOYEE", "123", "123");
        
        userService.createUser(Chef);
        userService.createUser(employee);
        
        // ChefAccount = userAccountManagement.create(Chef.getFirstName(), UnencryptedPassword.of("123"), Chef.getRolle());
        //UserAccount EmployeeAccount = userAccountManagement.create(employee.getFirstName(), UnencryptedPassword.of("123"), employee.getRolle());
    
        // ChefUser = new User(ChefAccount, Chef.getFirstName() + " " + Chef.getLastName(), Chef.getFirstName(), passwordEncoder.encode(Chef.getPassword()), Chef.getEmail(), UserRole.valueOf(Chef.getRolle()), true);
        // EmployeeUser = new User(EmployeeAccount, employee.getFirstName() + " " + employee.getLastName(), employee.getFirstName(), passwordEncoder.encode(employee.getPassword()), employee.getEmail(), UserRole.valueOf(employee.getRolle()), true);
        
        //.save(ChefUser);
        //.save(EmployeeUser);
        
        var password = "321";

        List.of(
            new RegistrierenForm("test", "test", "test@gmail.com", "EMPLOYEE", password, password),
            new RegistrierenForm("test1", "tester", "test1@gmail.com", "EMPLOYEE", password, password)
        ).forEach(userService::createUser);
    }
    
}
