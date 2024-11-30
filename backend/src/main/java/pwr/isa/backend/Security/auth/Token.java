package pwr.isa.backend.Security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pwr.isa.backend.User.User;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {

    private User assignedUser;
    private String token;
    private Date createdAt;
    private Date Due;
}
