package pharmacy.user;

import jakarta.validation.constraints.NotEmpty;

public class RegistrationForm {
    private final @NotEmpty String firstName, lastName, role, password, passwordConfirm; 
    private boolean isFromPersonal = false;

    /*@SuppressWarnings("unused")
    public RegistrierenForm(){}*/

    public RegistrationForm(String firstName, String lastName, String role, String password, String passwordConfirm){
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }
    
    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getRole(){
        return role;
    }

    public String getPassword(){
        return password;
    }

    public String getPasswordConfirm(){
        return passwordConfirm;
    }

    public Boolean getIsFromPersonal(){
        return isFromPersonal;
    }

    public Boolean setIsFromPersonal(boolean isFromPersonal){
        this.isFromPersonal = isFromPersonal;
        return this.isFromPersonal;
    }
}