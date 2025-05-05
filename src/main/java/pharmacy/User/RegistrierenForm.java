package pharmacy.User;

import jakarta.validation.constraints.NotEmpty;

public class RegistrierenForm {
    private final @NotEmpty String firstName, lastName, email, role, password, passwordConfirm; 
    private boolean isFromPersonal = false;

    /*@SuppressWarnings("unused")
    public RegistrierenForm(){}*/

    public RegistrierenForm(String firstName, String lastName, String email, String role, String password, String passwordConfirm){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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

    public String getEmail(){
        return email;
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