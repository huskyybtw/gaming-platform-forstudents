package pwr.isa.backend.Email;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pwr.isa.backend.User.UserService;

import java.io.InputStream;
import java.util.HashMap;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class EmailServiceImpl implements EmailService{

    private HashMap<String,Long> confirmations;
    private UserService userService;
    private JavaMailSender mailSender;

    @Value("${app.BASE_URL}")
    private String baseUrl;

    private InputStream HTMLinputStream = EmailServiceImpl.class.getResourceAsStream("/email-template.html");

    public EmailServiceImpl(@Lazy UserService userService, JavaMailSender mailSender) {
        this.userService = userService;
        this.mailSender = mailSender;
        confirmations = new HashMap<>();
    }


    @Override
    public void sendEmail(String userEmail, Long userId) throws Exception {
        String confirm = "test" + userEmail;

        String emailTemplate = new String(HTMLinputStream.readAllBytes(), UTF_8);

        String confirmationUrl = baseUrl + "/api/v1/register/?confirm=" + confirm;
        emailTemplate = emailTemplate.replace("${username}", userEmail);
        emailTemplate = emailTemplate.replace("${confirm-link}", confirmationUrl);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(userEmail);
        helper.setSubject("Email Confirmation");
        helper.setText(emailTemplate, true);

        mailSender.send(message);
        confirmations.put(confirm, userId);
    }

    @Override
    public void confirmEmail(String confirm) {
        Long userId = confirmations.get(confirm);
        if(userId != null) {
            userService.activateUser(userId);
            confirmations.remove(confirm);
        }
    }
}
