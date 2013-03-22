package unit.domain;

import bank.icbc.domain.Customer;
import bank.icbc.exception.CustomerException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.subethamail.wiser.Wiser;

import java.sql.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContextDataSource-test.xml", "classpath:applicationContext-servlet-test.xml"})
public class CustomerTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Customer customer;
    private Wiser wiser;

    @Before
    public void setUp() throws CustomerException {
        wiser = new Wiser();
        wiser.setPort(25000);
        wiser.start();

        customer = new Customer("dan", new Date(Date.valueOf("1982-10-12").getTime()), 100.00, "abc@test.com", false);
    }

    @After
    public void tearDown() {
        wiser.stop();
    }

    @Test
    public void should_throw_exception_when_add_customer_without_nickname() throws CustomerException {
        expectedException.expect(CustomerException.class);

        String nickName = "";
        customer.setNickname(nickName);
    }

    @Test
    public void should_throw_exception_when_nickname_contains_only_space() throws CustomerException {
        expectedException.expect(CustomerException.class);

        String nickName = "       ";
        customer.setNickname(nickName);
    }

    @Test
    public void should_throw_exception_when_nickname_contains_special_characters() throws CustomerException {
        expectedException.expect(CustomerException.class);
        String nickName = "^&*";
        customer.setNickname(nickName);
    }

    @Test
    public void should_throw_exception_when_nickname_contains_upper_case() throws CustomerException {
        expectedException.expect(CustomerException.class);
        String nickName = "A2s";
        customer.setNickname(nickName);
    }

    @Test
    public void should_set_nickname_successfully() throws CustomerException {
        String nickname = "bradpit";
        customer.setNickname(nickname);
    }

    @Test
    public void should_throw_exception_when_dateOfBirth_is_null() throws CustomerException {
        expectedException.expect(CustomerException.class);
        Date dateOfBirth = null;
        customer.setDateOfBirth(dateOfBirth);
    }

    @Test
    public void should_set_dateOfBirth_successfully() throws CustomerException {
        customer.setDateOfBirth(new Date(Date.valueOf("1988-10-02").getTime()));
    }

    @Test
    public void should_throw_exception_when_email_address_contains_special_character() throws CustomerException {
        expectedException.expect(CustomerException.class);

        String emailAddress = "%^&";
        customer.setEmailAddress(emailAddress);
    }

    @Test
    public void should_throw_exception_when_email_address_contains_two_at_icons() throws CustomerException {
        expectedException.expect(CustomerException.class);

        String emailAddress = "a@b@c.com";
        customer.setEmailAddress(emailAddress);
    }

    @Test
    public void should_throw_exception_when_email_address_is_empty() throws CustomerException {
        expectedException.expect(CustomerException.class);

        String emailAddress = "";
        customer.setEmailAddress(emailAddress);
    }

    @Test
    public void should_throw_exception_when_email_address_contains_only_white_space() throws CustomerException {
        expectedException.expect(CustomerException.class);

        String emailAddress = "    ";
        customer.setEmailAddress(emailAddress);
    }

    @Test
    public void should_set_email_address_successfully() throws CustomerException {
        String emailAddress = "abc@test.com";
        customer.setEmailAddress(emailAddress);
    }
}
