package pharmacy.User;

import jakarta.validation.constraints.NotEmpty;

public class RegistrierenForm {
    private final @NotEmpty String firstName, lastName, email, rolle, password, passwordConfirm; 

    // Konstruktor für manuelle Erstellung
    public RegistrierenForm(String firstName, String lastName, String email, String rolle, String password, String passwordConfirm){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.rolle = rolle;
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

    public String getRolle(){
        return rolle;
    }

    public String getPassword(){
        return password;
    }

    public String getPasswordConfirm(){
        return passwordConfirm;
    }
}
