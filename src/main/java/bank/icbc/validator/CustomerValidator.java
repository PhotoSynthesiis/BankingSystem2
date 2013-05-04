package bank.icbc.validator;

import java.sql.Date;
import java.util.regex.Pattern;

import static org.springframework.util.StringUtils.isEmpty;

public class CustomerValidator {
    public static boolean isNicknameValid(String nickname) {
        return !isStringEmpty(nickname) && isLowerLetterOrDigit(nickname);
    }

    public static boolean isEmailAddressValid(String emailAddress) {
        return !(isEmpty(emailAddress) || !isValidAddressFormat(emailAddress));
    }

    private static boolean isStringEmpty(String name) {
        return isEmpty(name);
    }

    private static boolean isLowerLetterOrDigit(String nickname) {
        Pattern pattern = Pattern.compile("[\\da-z]+");
        return pattern.matcher(nickname).matches();
    }

    public static boolean isDateOfBirthValid(Date dateOfBirth) {
        return dateOfBirth != null;
    }

    private static boolean isValidAddressFormat(String emailAddress) {
        Pattern pattern = Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
        return pattern.matcher(emailAddress).matches();
    }
}
