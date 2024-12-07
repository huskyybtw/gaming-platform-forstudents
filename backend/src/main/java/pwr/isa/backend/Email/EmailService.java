package pwr.isa.backend.Email;

public interface EmailService {
    void sendEmail(String userEmail,Long userId) throws  Exception;
    void confirmEmail(String confirm);
}
