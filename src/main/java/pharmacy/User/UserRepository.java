package pharmacy.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

import pharmacy.User.User.UserIdentifier;

interface UserRepository extends CrudRepository<User, UserIdentifier> {

    //alle Mitarbeiter finden
    @Override
	Streamable<User> findAll();
    
}
