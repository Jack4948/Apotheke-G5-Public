package pharmacy.user;

import java.io.Serializable;
import java.util.UUID;

import org.jmolecules.ddd.types.Identifier;
import org.salespointframework.core.AbstractAggregateRoot;
import org.salespointframework.useraccount.UserAccount;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

@Entity
public class User extends AbstractAggregateRoot<pharmacy.user.User.UserIdentifier>{

    private @EmbeddedId UserIdentifier id = new UserIdentifier();
    private String name;
    private String username;
    private String passwordHash;
    private String role;
    
    private boolean enabled;

    @OneToOne
    private UserAccount userAccount;

    protected User(){
        
    }

    public User(UserAccount userAccount, String name, String username, String passwordHash, String role, boolean enabled){
        this.userAccount = userAccount;
        this.name = name;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.enabled = enabled;
    }
    public UserAccount getUserAccount(){
        return userAccount;
    }

    public String getName(){
        return name;
    }

    public String getUsername(){
        return username;
    }

    public String getPasswordHash(){
        return passwordHash;
    }

    public String getRole(){
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
    
        private static final long serialVersionUID = 1L;
        private UUID id;

        public UserIdentifier() {
            this.id = UUID.randomUUID();
        }
        
        private UserIdentifier(UUID id) {
            this.id = id;
        }

        public static UserIdentifier of(String id) {
            return new UserIdentifier(UUID.fromString(id));
        }
        
        public static UserIdentifier of(UUID id) {
            return new UserIdentifier(id);
        }

        @Override
        public String toString() {
            return id.toString();
        }

        public UUID getId() {
            return id;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            UserIdentifier that = (UserIdentifier) obj;
            return id.equals(that.id);
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }
    }
}