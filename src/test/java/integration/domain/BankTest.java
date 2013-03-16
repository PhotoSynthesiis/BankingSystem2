package integration.domain;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContextDataSource-test.xml", "classpath:applicationContext-servlet.xml"})
public class BankTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    @Qualifier("bank")
    private Bank bank;

    private Customer customer;

    @Before
    public void setUp() throws NicknameInvalidException, DateOfBirthInvalidException {
//        customerDao.createTable(testTable);
        customer = new Customer("dan", new Date(Date.valueOf("1982-10-12").getTime()), 100.00);
    }

    @After
    public void tearDown() {
//        customerDao.deleteTable(testTable);
    }

    @Test
    public void should_add_customer_successfully() throws NicknameInvalidException, CustomerNotFoundException, DuplicateCustomerException, DateOfBirthInvalidException {
        bank.addCustomer(customer);

        Customer customerGet = bank.getCustomer("dan");
        assertThat("dan", is(customerGet.getNickname()));
    }

    @Test
    public void should_withdraw_successfully() throws NicknameInvalidException, DuplicateCustomerException, BalanceOverdrawException, DateOfBirthInvalidException {
        bank.addCustomer(customer);

        double balanceToWithdraw = 50.00;
        bank.withdraw(customer.getNickname(), balanceToWithdraw);
        double balanceGet = bank.getBalance(customer.getNickname());
        double expectedBalance = 50.00;

        assertThat(balanceGet, is(expectedBalance));
    }

    @Test
    public void should_throw_exception_when_add_customer_with_same_nickname() throws NicknameInvalidException, DuplicateCustomerException, DateOfBirthInvalidException {
        expectedException.expect(DuplicateCustomerException.class);

        bank.addCustomer(customer);
        bank.addCustomer(customer);
    }

    @Test
    public void should_withdraw_all_money_in_account_successfully() throws NicknameInvalidException, DuplicateCustomerException, BalanceOverdrawException, DateOfBirthInvalidException {
        bank.addCustomer(customer);

        double balanceToWithdraw = 100.00;
        bank.withdraw(customer.getNickname(), balanceToWithdraw);

        double balanceGet = bank.getBalance(customer.getNickname());
        double expectedBalance = 0.00;

        assertThat(balanceGet, is(expectedBalance));
    }

    @Test
    public void should_throw_exception_when_overdraw() throws NicknameInvalidException, DuplicateCustomerException, BalanceOverdrawException, DateOfBirthInvalidException {
        expectedException.expect(BalanceOverdrawException.class);

        bank.addCustomer(customer);
        double balanceToWithdraw = 200.00;
        bank.withdraw(customer.getNickname(), balanceToWithdraw);
    }

    @Test
    public void should_deposit_new_balance_successfully() throws DuplicateCustomerException, NicknameInvalidException, DateOfBirthInvalidException, BalanceOverdrawException {
        bank.addCustomer(customer);

        double balanceToDeposit = 23.00;
        bank.deposit(customer.getNickname(), balanceToDeposit);

        double expectedBalance = 123.00;
        assertThat(bank.getBalance(customer.getNickname()), is(expectedBalance));
    }
}
