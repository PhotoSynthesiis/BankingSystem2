package integration.domain;

import bank.icbc.domain.Bank;
import bank.icbc.domain.Customer;
import bank.icbc.domain.CustomerService;
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
@ContextConfiguration(locations = {"classpath:applicationContext-servlet-test.xml",
        "classpath:applicationContextDataSource-test.xml"})
public class CustomerServiceTest {
    private Wiser wiser;

    @Autowired
    @Qualifier("customerService")
    private CustomerService service;

    @Autowired
    @Qualifier("bank")
    private Bank bank;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        wiser = new Wiser();
        wiser.setPort(25000);
        wiser.start();
    }

    @After
    public void tearDown() {
        wiser.stop();
    }

    @Test
    @Transactional
    @Rollback(true)
    public void should_deposit_new_balance_successfully() throws DuplicateCustomerException, NicknameInvalidException,
            DateOfBirthInvalidException, BalanceOverdrawException, EmailAddressInvalidException {
        Customer customer1 = new Customer("dan", new Date(Date.valueOf("1988-09-03").getTime()), 310, "dan@test.com");

        bank.addCustomer(customer1);

        double balanceToDeposit = 23.00;
        service.deposit(customer1.getNickname(), balanceToDeposit);

        double expectedBalance = 333.00;
        double balance = service.getBalance(customer1.getNickname());

        assertThat(balance, is(expectedBalance));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void should_withdraw_successfully() throws NicknameInvalidException, DuplicateCustomerException,
            BalanceOverdrawException, DateOfBirthInvalidException, EmailAddressInvalidException {
        Customer customer1 = new Customer("dan", new Date(Date.valueOf("1988-09-03").getTime()), 310, "dan@test.com");

        bank.addCustomer(customer1);

        double balanceToWithdraw = 50.00;
        service.withdraw(customer1.getNickname(), balanceToWithdraw);

        double balance = service.getBalance(customer1.getNickname());
        double expectedBalance = 260.00;

        assertThat(balance, is(expectedBalance));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void should_withdraw_all_money_in_account_successfully() throws NicknameInvalidException, DuplicateCustomerException,
            BalanceOverdrawException, DateOfBirthInvalidException, EmailAddressInvalidException {
        Customer customer1 = new Customer("dan", new Date(Date.valueOf("1988-09-03").getTime()), 310, "dan@test.com");

        bank.addCustomer(customer1);

        double balanceToWithdraw = 310.00;
        service.withdraw(customer1.getNickname(), balanceToWithdraw);

        double balanceGet = service.getBalance(customer1.getNickname());
        double expectedBalance = 0.00;

        assertThat(balanceGet, is(expectedBalance));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void should_throw_exception_when_overdraw() throws NicknameInvalidException, DuplicateCustomerException,
            BalanceOverdrawException, DateOfBirthInvalidException, EmailAddressInvalidException {
        expectedException.expect(BalanceOverdrawException.class);

        Customer customer1 = new Customer("dan", new Date(Date.valueOf("1988-09-03").getTime()), 310, "dan@test.com");

        bank.addCustomer(customer1);
        double balanceToWithdraw = 400.00;
        service.withdraw(customer1.getNickname(), balanceToWithdraw);
    }
}
