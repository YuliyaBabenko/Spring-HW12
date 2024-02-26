package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import model.User;
import repository.IUserInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {
    @Autowired
    UserService us;
    @MockBean
    private IUserInterface repo;

    /**
     * Проверка метода добавления пользователя
     */
    @Test
    void addUserTest() {
        User user = createTestUser(1L);
        User createdUser = us.addUser(user);
        //проверки правильности возврата из метода
        assertEquals(createdUser.getId(), user.getId());
        assertEquals(createdUser.getName(), user.getName());
        assertEquals(createdUser.getLogin(), user.getLogin());
        assertEquals(createdUser.getPassword(), user.getPassword());
        //проверка числа обращений к репозиторию
        Mockito.verify(repo, Mockito.times(1)).save(user);
        Mockito.verify(repo, Mockito.times(1))
                .existsByLogin(ArgumentMatchers.eq(user.getLogin()));
    }

    /**
     * Проверка при добавлении ползователя с уже существующим login
     */
    @Test
    void addUserDuplicateTest() {
        //проверка записи дублирующего логина
        User user = createTestUser(1L);
        Mockito.doReturn(true)
                .when(repo)
                .existsByLogin(user.getLogin());

        User createdUser = us.addUser(user);
        //проверка числа вызовов репозиториев
        Mockito.verify(repo, Mockito.times(1))
                .existsByLogin(ArgumentMatchers.eq(user.getLogin()));
        Mockito.verify(repo, Mockito.times(0)).save(ArgumentMatchers.any());
        //проверка возврата null
        Assertions.assertNull(createdUser);
    }

    /**
     * Проверка получения всех пользователей из базы
     */
    @Test
    void getAllUsersTest() {
        //проверка получения пользователей из репозитория
        List<User> userList = new ArrayList<>();
        userList.add(createTestUser(0L));
        userList.add(createTestUser(1L));
        Mockito.doReturn(userList)
                .when(repo)
                .findAll();
        List<User> foundUsers = us.getAllUsers();
        assertEquals(foundUsers.get(0), userList.get(0));
        assertEquals(foundUsers.get(1), userList.get(1));
    }

    /**
     * Проверка получения пользователя по id
     */
    @Test
    void getUserByIdTest() {
        User user = createTestUser(1L);
        Mockito.doReturn(Optional.of(user)).when(repo)
                .findById(1L);
        User foundUser = us.getUserById(1L);
        Mockito.verify(repo, Mockito.times(1)).findById(1L);
        assertEquals(foundUser, user);
        foundUser = us.getUserById(2L);
        Mockito.verify(repo, Mockito.times(1)).findById(2L);
        Assertions.assertNull(foundUser);
    }

    /**
     * Проверка удаления ползователя по id
     */
    @Test
    void deleteUserByIdTest() {
        us.deleteUserById(1L);
        Mockito.verify(repo, Mockito.times(1)).deleteById(1L);
    }

    /**
     * Вспомогательный метод создания пользователя для целей тестирования.
     *
     * @param id создаваемого пользователя
     * @return new User(...)
     */
    private User createTestUser(long id) {
        return new User(id, "testname" + id, "testlogin" + id, "testpassword" + id);
    }

}
