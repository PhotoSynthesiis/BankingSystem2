package integration.service;

import bank.icbc.database.dao.CustomerDao;
import bank.icbc.database.service.CustomerService;
import bank.icbc.domain.Customer;
import bank.icbc.exception.BalanceOverdrawException;
import bank.icbc.exception.CustomerNotFoundException;
import bank.icbc.exception.DuplicateCustomerException;
import bank.icbc.exception.NicknameInvalidException;
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

    @Before
    public void setUp() {
        customerDao.createTable(testTable);
    }

    @After
    public void tearDown() {
        customerDao.deleteTable(testTable);
    }

    @Test
    public void shouldAddCustomerSuccessfully() throws NicknameInvalidException, CustomerNotFoundException, DuplicateCustomerException {
        Customer customerToBeSet = new Customer();
        customerToBeSet.setNickname("adam");
        customerToBeSet.setDateOfBirth(new Date(Date.valueOf("1983-10-25").getTime()));
        customerToBeSet.setBalance(100.00);

        customerService.addCustomer(customerToBeSet);

        Customer customerGet = customerService.getCustomer("adam");
        assertThat("adam", is(customerGet.getNickname()));
    }

    @Test
    public void shouldWithdrawMoneySuccessfully() throws NicknameInvalidException, DuplicateCustomerException, BalanceOverdrawException {
        Customer customerToBeSet = new Customer();
        customerToBeSet.setNickname("adam");
        customerToBeSet.setDateOfBirth(new Date(Date.valueOf("1983-10-25").getTime()));
        customerToBeSet.setBalance(100.00);

        customerService.addCustomer(customerToBeSet);

        double balanceToWithdraw = 50.00;
        customerService.withdraw(customerToBeSet.getNickname(), balanceToWithdraw);
        double balanceGet = customerService.getBalance(customerToBeSet.getNickname());
        double expectedBalance = 50.00;

        assertThat(balanceGet, is(expectedBalance));
    }

    @Test
    public void shouldThrowExceptionWhenAddTwoCustomersWithSameNickname() throws NicknameInvalidException, DuplicateCustomerException {
        expectedException.expect(DuplicateCustomerException.class);

        Customer customer1 = new Customer();
        customer1.setNickname("adam");
        customer1.setDateOfBirth(new Date(Date.valueOf("1983-10-25").getTime()));
        customer1.setBalance(100.00);

        Customer customer2 = new Customer();
        customer2.setNickname("adam");
        customer2.setDateOfBirth(new Date(Date.valueOf("1983-10-25").getTime()));
        customer2.setBalance(100.00);

        customerService.addCustomer(customer1);
        customerService.addCustomer(customer2);
    }

    @Test
    public void shouldWithdrawAllMoneyInAccountSuccessfully() throws NicknameInvalidException, DuplicateCustomerException, BalanceOverdrawException {
        Customer customerToBeSet = new Customer();
        customerToBeSet.setNickname("adam");
        customerToBeSet.setDateOfBirth(new Date(Date.valueOf("1983-10-25").getTime()));
        customerToBeSet.setBalance(100.00);

        customerService.addCustomer(customerToBeSet);

        double balanceToWithdraw = 100.00;
        customerService.withdraw(customerToBeSet.getNickname(), balanceToWithdraw);

        double balanceGet = customerService.getBalance(customerToBeSet.getNickname());
        double expectedBalance = 0.00;

        assertThat(balanceGet, is(expectedBalance));
    }

    @Test
    public void shouldThrowExceptionWhenTryOverWithdraw() throws NicknameInvalidException, DuplicateCustomerException, BalanceOverdrawException {
        expectedException.expect(BalanceOverdrawException.class);

        Customer customerToBeSet = new Customer();
        customerToBeSet.setNickname("adam");
        customerToBeSet.setDateOfBirth(new Date(Date.valueOf("1983-10-25").getTime()));
        customerToBeSet.setBalance(100.00);

        customerService.addCustomer(customerToBeSet);
        double balanceToWithdraw = 200.00;
        customerService.withdraw(customerToBeSet.getNickname(), balanceToWithdraw);
    }

    @Test
    public void shouldDepositNewBalanceSuccessfully() throws DuplicateCustomerException, NicknameInvalidException {
        Customer customerToBeSet = new Customer();
        customerToBeSet.setNickname("adam");
        customerToBeSet.setDateOfBirth(new Date(Date.valueOf("1983-10-25").getTime()));
        customerToBeSet.setBalance(100.00);

        customerService.addCustomer(customerToBeSet);

        double balanceToDeposit = 23.00;
        customerService.deposit(customerToBeSet.getNickname(), balanceToDeposit);

        double expectedBalance = 123.00;
        assertThat(customerService.getBalance(customerToBeSet.getNickname()), is(expectedBalance));
    }
}
