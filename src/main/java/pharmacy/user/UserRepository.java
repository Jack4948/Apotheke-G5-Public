package pharmacy.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

import pharmacy.user.User.UserIdentifier;

public interface UserRepository extends CrudRepository<User, UserIdentifier> {

    @Override
	Streamable<User> findAll();
}