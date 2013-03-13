package unit.domain;

import bank.icbc.domain.Customer;
import bank.icbc.exception.NicknameInvalidException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CustomerTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void should_throw_exception_when_add_customer_without_nickname() throws NicknameInvalidException {
        expectedException.expect(NicknameInvalidException.class);

        String nickName = "";

        Customer customer = new Customer();
        customer.setNickname(nickName);
    }

    @Test
    public void should_throw_exception_when_nickname_contains_only_space() throws NicknameInvalidException {
        expectedException.expect(NicknameInvalidException.class);

        String nickName = "       ";

        Customer customer = new Customer();
        customer.setNickname(nickName);
    }

    @Test
    public void should_throw_exception_when_nickname_contains_upper_case() throws NicknameInvalidException {
        expectedException.expect(NicknameInvalidException.class);

        String nickName = "A2s";

        Customer customer = new Customer();
        customer.setNickname(nickName);
    }

    @Test
    public void should_add_customer_successfully() throws NicknameInvalidException {
        String nickname = "bradpit";

        Customer customer = new Customer();
        customer.setNickname(nickname);

        String expectedNickname = nickname;
        assertThat(expectedNickname, is(customer.getNickname()));
    }


}
