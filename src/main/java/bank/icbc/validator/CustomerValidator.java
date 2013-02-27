package bank.icbc.validator;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

public class CustomerValidator {
    public static boolean isNicknameValid(String nickname) {
        if (isEmpty(nickname)) {
            return false;
        } else {
            return isLowerLetterOrDigit(nickname);
        }
    }

    private static boolean isEmpty(String name) {
        return StringUtils.isEmpty(name);
    }

    private static boolean isLowerLetterOrDigit(String nickname) {
        boolean containInvalidCharacters;
        Pattern pattern = Pattern.compile("[\\da-z]+");
        containInvalidCharacters = pattern.matcher(nickname).matches();
        return containInvalidCharacters;
    }
}
