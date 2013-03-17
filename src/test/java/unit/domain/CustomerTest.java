package unit.domain;

import bank.icbc.domain.Bank;
import bank.icbc.domain.Customer;
import bank.icbc.exception.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.subethamail.wiser.Wiser;

import java.sql.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContextDataSource-test.xml", "classpath:applicationContext-servlet-test.xml"})
public class CustomerTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    @Qualifier("bank")
    private Bank bank;

    private Customer customerToBeSet;
    private Wiser wiser;

    @Autowired
    @Qualifier("customer")
    private Customer customer;

    @Before
    public void setUp() throws NicknameInvalidException, DateOfBirthInvalidException, EmailAddressInvalidException {
        wiser = new Wiser();
        wiser.setPort(25000);
        wiser.start();

        customerToBeSet = new Customer("dan", new Date(Date.valueOf("1982-10-12").getTime()), 100.00, "abc@test.com");
    }

    @After
    public void tearDown() {
        wiser.stop();
    }

    @Test
    public void should_throw_exception_when_add_customer_without_nickname() throws NicknameInvalidException {
        expectedException.expect(NicknameInvalidException.class);

        String nickName = "";
        customerToBeSet.setNickname(nickName);
    }

    @Test
    public void should_throw_exception_when_nickname_contains_only_space() throws NicknameInvalidException {
        expectedException.expect(NicknameInvalidException.class);

        String nickName = "       ";
        customerToBeSet.setNickname(nickName);
    }

    @Test
    public void should_throw_exception_when_nickname_contains_special_characters() throws NicknameInvalidException {
        expectedException.expect(NicknameInvalidException.class);
        String nickName = "^&*";
        customerToBeSet.setNickname(nickName);
    }

    @Test
    public void should_throw_exception_when_nickname_contains_upper_case() throws NicknameInvalidException {
        expectedException.expect(NicknameInvalidException.class);
        String nickName = "A2s";
        customerToBeSet.setNickname(nickName);
    }

    @Test
    public void should_set_nickname_successfully() throws NicknameInvalidException {
        String nickname = "bradpit";
        customerToBeSet.setNickname(nickname);
    }

    @Test
    public void should_throw_exception_when_dateOfBirth_is_null() throws NicknameInvalidException, DateOfBirthInvalidException {
        expectedException.expect(DateOfBirthInvalidException.class);
        Date dateOfBirth = null;
        customerToBeSet.setDateOfBirth(dateOfBirth);
    }

    @Test
    public void should_set_dateOfBirth_successfully() throws DateOfBirthInvalidException {
        customerToBeSet.setDateOfBirth(new Date(Date.valueOf("1988-10-02").getTime()));
    }

    @Test
    public void should_throw_exception_when_email_address_contains_special_character() throws EmailAddressInvalidException {
        expectedException.expect(EmailAddressInvalidException.class);

        String emailAddress = "%^&";
        customerToBeSet.setEmailAddress(emailAddress);
    }

    @Test
    public void should_throw_exception_when_email_address_contains_two_at_icons() throws EmailAddressInvalidException {
        expectedException.expect(EmailAddressInvalidException.class);

        String emailAddress = "a@b@c.com";
        customerToBeSet.setEmailAddress(emailAddress);
    }

    @Test
    public void should_throw_exception_when_email_address_is_empty() throws EmailAddressInvalidException {
        expectedException.expect(EmailAddressInvalidException.class);

        String emailAddress = "";
        customerToBeSet.setEmailAddress(emailAddress);
    }

    @Test
    public void should_throw_exception_when_email_address_contains_only_white_space() throws EmailAddressInvalidException {
        expectedException.expect(EmailAddressInvalidException.class);

        String emailAddress = "    ";
        customerToBeSet.setEmailAddress(emailAddress);
    }

    @Test
    public void should_set_email_address_successfully() throws EmailAddressInvalidException {
        String emailAddress = "abc@test.com";
        customerToBeSet.setEmailAddress(emailAddress);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void should_deposit_new_balance_successfully() throws DuplicateCustomerException, NicknameInvalidException, DateOfBirthInvalidException, BalanceOverdrawException {
        bank.addCustomer(customerToBeSet);

        double balanceToDeposit = 23.00;
        customer.deposit(customerToBeSet.getNickname(), balanceToDeposit);

        double expectedBalance = 123.00;
        assertThat(customer.getBalanceInAccount(customerToBeSet.getNickname()), is(expectedBalance));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void should_withdraw_successfully() throws NicknameInvalidException, DuplicateCustomerException, BalanceOverdrawException, DateOfBirthInvalidException {
        bank.addCustomer(customerToBeSet);

        double balanceToWithdraw = 50.00;
        customer.withdraw(customerToBeSet.getNickname(), balanceToWithdraw);
        double balanceGet = customer.getBalanceInAccount(customerToBeSet.getNickname());
        double expectedBalance = 50.00;

        assertThat(balanceGet, is(expectedBalance));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void should_withdraw_all_money_in_account_successfully() throws NicknameInvalidException, DuplicateCustomerException, BalanceOverdrawException, DateOfBirthInvalidException {
        bank.addCustomer(customerToBeSet);

        double balanceToWithdraw = 100.00;
        customer.withdraw(customerToBeSet.getNickname(), balanceToWithdraw);

        double balanceGet = customer.getBalanceInAccount(customerToBeSet.getNickname());
        double expectedBalance = 0.00;

        assertThat(balanceGet, is(expectedBalance));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void should_throw_exception_when_overdraw() throws NicknameInvalidException, DuplicateCustomerException, BalanceOverdrawException, DateOfBirthInvalidException {
        expectedException.expect(BalanceOverdrawException.class);

        bank.addCustomer(customerToBeSet);
        double balanceToWithdraw = 200.00;
        customer.withdraw(customerToBeSet.getNickname(), balanceToWithdraw);
    }
}
