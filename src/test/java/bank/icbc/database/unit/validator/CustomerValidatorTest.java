package bank.icbc.database.unit.validator;

import bank.icbc.validator.CustomerValidator;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CustomerValidatorTest {
    @Test
    public void shouldReturnFalseWhenNicknameIsInvalid_1() {
        String nickname = "ABC";
        boolean actual = CustomerValidator.isNicknameValid(nickname);
        boolean expected = false;

        assertThat(expected, is(actual));
    }

    @Test
    public void shouldReturnFalseWhenNicknameIsInvalid_2() {
        String nickname = "$%^";
        boolean actual = CustomerValidator.isNicknameValid(nickname);
        boolean expected = false;

        assertThat(expected, is(actual));
    }

    @Test
    public void shouldReturnFalseWhenNicknameIsInvalid_3() {
        String nickname = null;
        boolean actual = CustomerValidator.isNicknameValid(nickname);
        boolean expected = false;

        assertThat(expected, is(actual));
    }

    @Test
    public void shouldReturnFalseWhenNicknameIsInvalid_4() {
        String nickname = "";
        boolean actual = CustomerValidator.isNicknameValid(nickname);
        boolean expected = false;

        assertThat(expected, is(actual));
    }

    @Test
    public void shouldReturnFalseWhenNicknameIsInvalid_5() {
        String nickname = "  ";
        boolean actual = CustomerValidator.isNicknameValid(nickname);
        boolean expected = false;

        assertThat(expected, is(actual));
    }

    @Test
    public void shouldReturnTrueWhenNicknameIsValid() {
        String nickname = "nick";
        boolean actual = CustomerValidator.isNicknameValid(nickname);
        boolean expected = true;

        assertThat(expected, is(actual));
    }
}
