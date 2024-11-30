package pwr.isa.backend.Email;

public interface EmailService {
    void sendEmail(String userEmail,Long userId);
    void confirmEmail(String confirm);
}
