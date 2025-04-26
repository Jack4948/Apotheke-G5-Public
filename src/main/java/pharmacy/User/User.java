package pharmacy.User;

import java.io.Serializable;
import java.util.UUID;

import org.jmolecules.ddd.types.Identifier;
import org.salespointframework.core.AbstractAggregateRoot;
import org.salespointframework.useraccount.UserAccount;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;

@Entity
public class User extends AbstractAggregateRoot<pharmacy.User.User.UserIdentifier>{

    private @EmbeddedId UserIdentifier id = new UserIdentifier();
    private String name;
    private String username;
    private String passwordHash;
    private String email;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    
    private boolean enabled;

    @OneToOne
    private UserAccount userAccount;

    public User(UserAccount userAccount, String name, String username, String passwordHash, String email, UserRole role, boolean enabled){
        this.userAccount = userAccount;
        this.name = name;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.role = role;
        this.enabled = enabled;
    }
    public UserAccount getUserAccount(){
        return userAccount;
    }

    public String getName(){
        return name;
    }

    public String GetUsername(){ //
        return username;
    }

    public String getPasswordHash(){
        return passwordHash;
    }

    public String getEmail(){
        return email;
    }

    public UserRole getRole(){
        return role;
    }

    public boolean getEnabled(){
        return enabled;
    }

    @Override
    public UserIdentifier getId(){
        return id;
    }

    public static final class UserIdentifier implements Identifier, Serializable{
    
        private static final long serialVersionUID = 7740660930809051850L;
		private final UUID identifier;

        UserIdentifier(){
            this(UUID.randomUUID());
        }

        public UserIdentifier(UUID identifier) {
            this.identifier = identifier;
        } 
        
        @Override
		public int hashCode(){

			final int prime = 31;
			int result = 1;

			result = prime * result + (identifier == null ? 0 : identifier.hashCode());

			return result;
		}

        @Override
		public boolean equals(Object obj){

			if (obj == this) {
				return true;
			}

			if (!(obj instanceof UserIdentifier that)) {
				return false;
			}

			return this.identifier.equals(that.identifier);
		}
    }
}
