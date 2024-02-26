package repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import model.User;

@Repository
public interface IUserInterface extends CrudRepository<User, Long> {

    boolean existsByLogin(String login);
}
