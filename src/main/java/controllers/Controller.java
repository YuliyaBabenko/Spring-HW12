package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import model.User;
import observer.UserCreateEvent;
import service.FileLoggerGateway;
import service.UserService;

import java.util.List;

/* Контроллер работы с пользователями
 * GET /users - получить все логины пользователей
 * GET /users/{id} - получить пользователя с указанным id
 * POST /users - добавить пользователя ( в запросе должен быть пердан JSON{name,login,password})
 * DELETE /users/{id} - удалить пользователя с указанным id
 */
@RestController
public class Controller {
    @Autowired
    UserService userService;
    @Autowired
    FileLoggerGateway fileLoggerGateway;
    @Autowired
    ApplicationEventPublisher publisher;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/users/add")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        User createdUser = userService.addUser(user);
        if (createdUser == null) return ResponseEntity.badRequest().build();
        publisher.publishEvent(new UserCreateEvent(this, user));
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delUserById(@PathVariable long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }
}
