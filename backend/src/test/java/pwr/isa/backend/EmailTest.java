package pwr.isa.backend;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmailTest {

    @Test
    public void testInvalidEmails() {
        List<String> invalidEmails = List.of(
                "",
                "@@@@gmail.com",
                "@aaaaaaaaaaaaa@gmail.com",
                "@gmail.com@gmail.com",
                "@gmail.com",
                "plainaddress",
                "plain@.com",
                "plain@gmail",
                "plain@gmail..com",
                "plain@-gmail.com",
                "plain@gmail.c",
                "plain@@gmail.com"
        );

        for (String email : invalidEmails) {
            assertFalse(EmailValidator.isValid(email),
                    "Expected email to be invalid: " + email);
        }
    }

    @Test
    public void testValidEmails() {
        List<String> validEmails = List.of(
                "test@gmail.com",
                "marek.kocik@gmail.com",
                "test123.@gmail.com"
        );

        for (String email : validEmails) {
            assertTrue(EmailValidator.isValid(email),
                    "Expected email to be valid: " + email);
        }
    }
}
