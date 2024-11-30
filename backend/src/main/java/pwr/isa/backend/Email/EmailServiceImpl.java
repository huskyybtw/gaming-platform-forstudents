package pwr.isa.backend.Email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pwr.isa.backend.User.UserService;

import java.util.HashMap;

@Service
public class EmailServiceImpl implements EmailService{
    private HashMap<String,Long> confirmations;
    private UserService userService;
    private JavaMailSender mailSender;
    @Value("${app.BASE_URL}")
    private String baseUrl;

    public EmailServiceImpl(@Lazy UserService userService, JavaMailSender mailSender) {
        this.userService = userService;
        this.mailSender = mailSender;
        confirmations = new HashMap<>();
    }


    @Override
    public void sendEmail(String userEmail,Long userId) {
        String confirm = "test";
        confirmations.put(confirm,userId);

        String subject = "Email Confirmation";
        String message = "Please confirm your email by clicking the following link: " +
                baseUrl + "/api/v1/register/?confirm=" + confirm;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(userEmail);
        email.setSubject(subject);
        email.setText(message);

        mailSender.send(email);
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
