package integration.service;

import bank.icbc.database.dao.CustomerDao;
import bank.icbc.database.service.CustomerService;
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
public class CustomerServiceTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private String testTable = "customer";

    @Autowired
    @Qualifier("customerService")
    private CustomerService customerService;

    @Autowired
    @Qualifier("customerDao")
    private CustomerDao customerDao;

    private Customer customer;

    @Before
    public void setUp() throws NicknameInvalidException, DateOfBirthInvalidException {
        customerDao.createTable(testTable);
        customer = new Customer("dan", new Date(Date.valueOf("1982-10-12").getTime()), 100.00);
    }

    @After
    public void tearDown() {
        customerDao.deleteTable(testTable);
    }

    @Test
    public void should_add_customer_successfully() throws NicknameInvalidException, CustomerNotFoundException, DuplicateCustomerException, DateOfBirthInvalidException {
        customerService.addCustomer(customer);

        Customer customerGet = customerService.getCustomer("dan");
        assertThat("dan", is(customerGet.getNickname()));
    }

    @Test
    public void should_withdraw_successfully() throws NicknameInvalidException, DuplicateCustomerException, BalanceOverdrawException, DateOfBirthInvalidException {
        customerService.addCustomer(customer);

        double balanceToWithdraw = 50.00;
        customerService.withdraw(customer.getNickname(), balanceToWithdraw);
        double balanceGet = customerService.getBalance(customer.getNickname());
        double expectedBalance = 50.00;

        assertThat(balanceGet, is(expectedBalance));
    }

    @Test
    public void should_throw_exception_when_add_customer_with_same_nickname() throws NicknameInvalidException, DuplicateCustomerException, DateOfBirthInvalidException {
        expectedException.expect(DuplicateCustomerException.class);

        customerService.addCustomer(customer);
        customerService.addCustomer(customer);
    }

    @Test
    public void should_withdraw_all_money_in_account_successfully() throws NicknameInvalidException, DuplicateCustomerException, BalanceOverdrawException, DateOfBirthInvalidException {
        customerService.addCustomer(customer);

        double balanceToWithdraw = 100.00;
        customerService.withdraw(customer.getNickname(), balanceToWithdraw);

        double balanceGet = customerService.getBalance(customer.getNickname());
        double expectedBalance = 0.00;

        assertThat(balanceGet, is(expectedBalance));
    }

    @Test
    public void should_throw_exception_when_overdraw() throws NicknameInvalidException, DuplicateCustomerException, BalanceOverdrawException, DateOfBirthInvalidException {
        expectedException.expect(BalanceOverdrawException.class);

        customerService.addCustomer(customer);
        double balanceToWithdraw = 200.00;
        customerService.withdraw(customer.getNickname(), balanceToWithdraw);
    }

    @Test
    public void should_deposit_new_balance_successfully() throws DuplicateCustomerException, NicknameInvalidException, DateOfBirthInvalidException {
        customerService.addCustomer(customer);

        double balanceToDeposit = 23.00;
        customerService.deposit(customer.getNickname(), balanceToDeposit);

        double expectedBalance = 123.00;
        assertThat(customerService.getBalance(customer.getNickname()), is(expectedBalance));
    }
}
