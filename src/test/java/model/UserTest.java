package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void userTest(){
        User user = new User(1,"testname","testlogin","testpassword");
        assertEquals(user.getId(),1);
        assertEquals(user.getName(),"testname");
        assertEquals(user.getPassword(),"testpassword");
    }
}
