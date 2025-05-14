package pharmacy.user;

import java.util.List;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.QRole;
import org.salespointframework.useraccount.UserAccountManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import pharmacy.personal.PersonalService;

@Component
@Order(10)
public class UserDataInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(UserDataInitializer.class);

	private final UserAccountManagement userAccountManagement;
	private final UserService userService;
	private final PersonalService personlService;

	public UserDataInitializer(UserAccountManagement userAccountManagement, PersonalService personlService, UserService userService) {
		Assert.notNull(userAccountManagement, "UserAccount muss nicht null sein");
		Assert.notNull(userService, "UserService muss nicht null sein");
		Assert.notNull(personlService, "PersonalService muss nicht null sein ");
		this.personlService = personlService;
		this.userAccountManagement = userAccountManagement;
		this.userService = userService;

	}

	@Override
	public void initialize() {

		if (userAccountManagement.findAll().iterator().hasNext()) {
			return;

		}
		LOG.info("Erstelle Standard-Benutzer");

		RegistrationForm chef = new RegistrationForm("chef", "chef", "BOSS", "123", "123");
		RegistrationForm apotheker = new RegistrationForm("emp", "emp", "EMPLOYEE", "123", "123");

		RegistrationForm lieferdienst = new RegistrationForm("fahrer", "fahrer", "DELIVERY_DRIVER", "123", "123");

		userService.createUser(chef);
		userService.createUser(apotheker);

		userService.createUser(lieferdienst);

		var password = "321";

		List.of(
			new RegistrationForm("test", "test", "EMPLOYEE", password, password),
			new RegistrationForm("test1", "tester", "BOSS", password, password)
		).forEach(userService::createUser);
		// aktiviere initiale Benutzer
		personlService.activateInitialUsers();
	}

}