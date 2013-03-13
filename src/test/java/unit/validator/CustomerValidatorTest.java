package unit.validator;

import bank.icbc.validator.CustomerValidator;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CustomerValidatorTest {
    @Test
    public void should_return_false_when_nickname_contains_only_uppercase_letters() {
        String nickname = "ABC";
        boolean actual = CustomerValidator.isNicknameValid(nickname);
        boolean expected = false;

        assertThat(expected, is(actual));
    }

    @Test
    public void should_return_false_when_nickname_contains_special_characters() {
        String nickname = "$%^";
        boolean actual = CustomerValidator.isNicknameValid(nickname);
        boolean expected = false;

        assertThat(expected, is(actual));
    }

    @Test
    public void should_return_false_when_nickname_is_null() {
        String nickname = null;
        boolean actual = CustomerValidator.isNicknameValid(nickname);
        boolean expected = false;

        assertThat(expected, is(actual));
    }

    @Test
    public void should_return_false_when_nickname_is_empty() {
        String nickname = "";
        boolean actual = CustomerValidator.isNicknameValid(nickname);
        boolean expected = false;

        assertThat(expected, is(actual));
    }

    @Test
    public void should_return_false_when_nickname_contains_only_space() {
        String nickname = "  ";
        boolean actual = CustomerValidator.isNicknameValid(nickname);
        boolean expected = false;

        assertThat(expected, is(actual));
    }

    @Test
    public void should_return_true_when_nickname_is_valid() {
        String nickname = "nick";
        boolean actual = CustomerValidator.isNicknameValid(nickname);
        boolean expected = true;

        assertThat(expected, is(actual));
    }
}
