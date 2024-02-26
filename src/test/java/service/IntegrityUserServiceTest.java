package service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IntegrityUserServiceTest {
    @Autowired
    UserService us;

    /**
     * Тест создания пользователя, запись в базу, получение из базу
     */
    @Test
    public void integrityUserServiceTest(){
        User user = new User();
        user.setName("test-user");
        user.setLogin("test-login");
        user.setPassword("test-password");
        User createdUser = us.addUser(user);
        long id = createdUser.getId();
        User readUser = us.getUserById(id);
        assertEquals(readUser,createdUser);
        us.deleteUserById(id);
        readUser = us.getUserById(id);
        assertNull(readUser);
    }
}
