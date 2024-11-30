package pwr.isa.backend.Email;

public class EmailValidator {
    public static boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";


        long atCount = email.chars().filter(ch -> ch == '@').count();
        if (atCount != 1) {
            return false;
        }

        if (!email.matches(emailRegex)) {
            return false;
        }

        // @student.pwr.edu.pl @gmail as placeholder
        if(!email.endsWith("@gmail.com")){
            return false;
        }
        return true;
    }
}
