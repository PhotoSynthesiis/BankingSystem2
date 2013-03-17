package bank.icbc.validator;

import org.springframework.util.StringUtils;

import java.sql.Date;
import java.util.regex.Pattern;

public class CustomerValidator {
    public static boolean isNicknameValid(String nickname) {
        if (isEmpty(nickname)) {
            return false;
        } else {
            return isLowerLetterOrDigit(nickname);
        }
    }

    public static boolean isEmailAddressValid(String emailAddress) {
        if (StringUtils.isEmpty(emailAddress) || !isValidAddressFormat(emailAddress)) {
            return false;
        }
        return true;
    }

    private static boolean isEmpty(String name) {
        return StringUtils.isEmpty(name);
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
