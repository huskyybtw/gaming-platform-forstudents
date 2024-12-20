package pwr.isa.backend.Email;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/register")
@Tag(name = "Email", description = "The Email API for handling email confirmations")
public class EmailControler {
    private EmailService emailService;

    public EmailControler(EmailService emailService) {
        this.emailService = emailService;
    }
    @Operation(summary = "Confirm email", description = "Endpoint to confirm user registration via email")
    @GetMapping(path= "/")
    public void ReciveEmail(@RequestParam("confirm") String confirm) {
        emailService.confirmEmail(confirm);
    }
}