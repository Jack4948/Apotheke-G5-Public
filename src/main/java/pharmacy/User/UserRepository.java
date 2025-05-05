package pharmacy.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

import pharmacy.User.User.UserIdentifier;

public interface UserRepository extends CrudRepository<User, UserIdentifier> {

    @Override
	Streamable<User> findAll();
}
