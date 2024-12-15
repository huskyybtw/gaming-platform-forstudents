package pwr.isa.backend.Email;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/register")
@Tag(name = "Email", description = "The Email API")
public class EmailControler {
    private EmailService emailService;

    public EmailControler(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping(path= "/")
    public void ReciveEmail(@RequestParam("confirm") String confirm) {
        emailService.confirmEmail(confirm);
    }
}