package pwr.isa.backend.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceTest {

    private final UserService userService;

    @Autowired
    public UserServiceTest(UserService userServiceImpl) {
        this.userService = userServiceImpl;
    }

    @Test
    public void testThatUserIsCreatedAndSavedCorrectlyInDB() {
        User user = UserDataUtil.createValidUser();

        userService.createUser(user);
        assertThat(userService.getUserById(user.getID())).isEqualTo(user);
    }

    @Test
    public void testThatUserWithSameEmailisNotCreated() {
        User user = UserDataUtil.createValidUser();
        userService.createUser(user);
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
    }
}
