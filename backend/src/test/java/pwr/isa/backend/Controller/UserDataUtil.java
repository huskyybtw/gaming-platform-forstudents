package pwr.isa.backend.Controller;

import lombok.NoArgsConstructor;
import pwr.isa.backend.entity.User;
import pwr.isa.backend.entity.UserRole;

@NoArgsConstructor
public final class UserDataUtil {

    public static User createValidUser() {
        return User.builder()
                .ID(1L)
                .email("marek@gmail.com")
                .password("password")
                .role(UserRole.USER)
                .build();
    }

    public static User createOtherValidUser() {
        return User.builder()
                .ID(1L)
                .email("test@gmail.com")
                .password("test")
                .role(UserRole.USER)
                .build();
    }

    public static User createInvalidUserNoEmail() {
        return User.builder()
                .ID(1L)
                .email("marek")
                .password("password")
                .role(UserRole.USER)
                .build();
    }

    public static User createInvalidUserNoPassword() {
        return User.builder()
                .ID(1L)
                .email("marek123@gmail.com")
                .role(UserRole.USER)
                .build();
    }
}
