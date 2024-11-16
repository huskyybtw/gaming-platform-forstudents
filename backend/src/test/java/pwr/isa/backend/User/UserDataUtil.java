package pwr.isa.backend.User;

import lombok.NoArgsConstructor;

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

}
