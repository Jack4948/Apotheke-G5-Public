package pharmacy.personal;

import pharmacy.user.RegistrationForm;

public class ClinicRegistrierenForm extends RegistrationForm {

	public ClinicRegistrierenForm(String firstName, String lastName, String email, String password, String passwordConfirm) {
		super(firstName, lastName, "DOCTORS_OFFICE", password, passwordConfirm);
		this.setIsFromPersonal(true);
	}

}