package pharmacy.user;

import jakarta.validation.constraints.NotEmpty;

public class RegistrationForm {
    private final @NotEmpty String firstName;
    private final @NotEmpty String lastName;
    private @NotEmpty String role;
    private final @NotEmpty String password;
    private final @NotEmpty String passwordConfirm;
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

    public void setIsFromPersonal(boolean isFromPersonal){
        this.isFromPersonal = isFromPersonal;

    }

    public void setRole(String role){
        this.role = role;
    }
}