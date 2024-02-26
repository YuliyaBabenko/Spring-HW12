package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import model.User;
import repository.IUserInterface;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    IUserInterface repo;


    public User addUser(User user) {
        if (repo.existsByLogin(user.getLogin())) return null;
        repo.save(user);
        return user;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        repo.findAll().forEach(users::add);
        return users;
    }

    public User getUserById(long id) {
        return repo.findById(id).orElse(null);
    }

    public void deleteUserById(long id) {
        repo.deleteById(id);
    }
}
