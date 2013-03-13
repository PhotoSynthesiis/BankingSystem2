package unit.domain;

import bank.icbc.domain.Customer;
import bank.icbc.exception.DateOfBirthInvalidException;
import bank.icbc.exception.NicknameInvalidException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.Date;

public class CustomerTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Customer customer;

    @Before
    public void setUp() throws NicknameInvalidException, DateOfBirthInvalidException {
        customer = new Customer("dan", new Date(Date.valueOf("1982-10-12").getTime()), 100.00);
    }

    @Test
    public void should_throw_exception_when_add_customer_without_nickname() throws NicknameInvalidException {
        expectedException.expect(NicknameInvalidException.class);

        String nickName = "";
        customer.setNickname(nickName);
    }

    @Test
    public void should_throw_exception_when_nickname_contains_only_space() throws NicknameInvalidException {
        expectedException.expect(NicknameInvalidException.class);

        String nickName = "       ";
        customer.setNickname(nickName);
    }

    @Test
    public void should_throw_exception_when_nickname_contains_special_characters() throws NicknameInvalidException {
        expectedException.expect(NicknameInvalidException.class);
        String nickName = "^&*";
        customer.setNickname(nickName);
    }

    @Test
    public void should_throw_exception_when_nickname_contains_upper_case() throws NicknameInvalidException {
        expectedException.expect(NicknameInvalidException.class);
        String nickName = "A2s";
        customer.setNickname(nickName);
    }

    @Test
    public void should_set_nickname_successfully() throws NicknameInvalidException {
        String nickname = "bradpit";
        customer.setNickname(nickname);
    }

    @Test
    public void should_throw_exception_when_dateOfBirth_is_null() throws NicknameInvalidException, DateOfBirthInvalidException {
        expectedException.expect(DateOfBirthInvalidException.class);
        Date dateOfBirth = null;
        customer.setDateOfBirth(dateOfBirth);
    }

    @Test
    public void should_set_dateOfBirth_successfully() throws DateOfBirthInvalidException {
        customer.setDateOfBirth(new Date(Date.valueOf("1988-10-02").getTime()));
    }
}
