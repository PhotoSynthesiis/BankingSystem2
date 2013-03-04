package bank.icbc.database.integration;

import bank.icbc.database.template.CustomerDAO;
import bank.icbc.domain.Customer;
import bank.icbc.exception.BalanceOverdrawException;
import bank.icbc.exception.CustomerNotFoundException;
import bank.icbc.exception.DuplicateCustomerException;
import bank.icbc.exception.NicknameInvalidException;
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
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/applicationContextDataSource.xml")
public class CustomerDAOTest {

    @Autowired
    @Qualifier("customerDAO")
    private CustomerDAO customerDAO;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private String testTable = "testCustomer";

    @Before
    public void setUp() {
        customerDAO.createTable(testTable);
    }

    @Test
    public void shouldAddCustomerSuccessfully() throws NicknameInvalidException, CustomerNotFoundException, DuplicateCustomerException {
        Customer customerToBeSet = new Customer();
        customerToBeSet.setNickname("adam");
        customerToBeSet.setDateOfBirth(new Date(Date.valueOf("1983-10-25").getTime()));
        customerToBeSet.setBalance(100.00);

        customerDAO.addCustomer(customerToBeSet, testTable);

        Customer customerGet = customerDAO.getCustomer("adam", testTable);
        assertThat("adam", is(customerGet.getNickname()));
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

        customerDAO.addCustomer(customer1, testTable);
        customerDAO.addCustomer(customer2, testTable);
    }

    @Test
    public void shouldWithdrawMoneySuccessfully() throws NicknameInvalidException, DuplicateCustomerException, BalanceOverdrawException {
        Customer customerToBeSet = new Customer();
        customerToBeSet.setNickname("adam");
        customerToBeSet.setDateOfBirth(new Date(Date.valueOf("1983-10-25").getTime()));
        customerToBeSet.setBalance(100.00);

        customerDAO.addCustomer(customerToBeSet, testTable);

        double balanceToWithdraw = 50.00;
        customerDAO.withdrawBalance(customerToBeSet.getNickname(), balanceToWithdraw, testTable);
        double balanceGet = customerDAO.getBalance(customerToBeSet.getNickname(), testTable);
        double expectedBalance = 50.00;

        assertThat(balanceGet, is(expectedBalance));
    }

    @Test
    public void shouldWithdrawAllMoneyInAccountSuccessfully() throws NicknameInvalidException, DuplicateCustomerException, BalanceOverdrawException {
        Customer customerToBeSet = new Customer();
        customerToBeSet.setNickname("adam");
        customerToBeSet.setDateOfBirth(new Date(Date.valueOf("1983-10-25").getTime()));
        customerToBeSet.setBalance(100.00);

        customerDAO.addCustomer(customerToBeSet, testTable);
        double balanceToWithdraw = 100.00;
        customerDAO.withdrawBalance(customerToBeSet.getNickname(), balanceToWithdraw, testTable);
        double balanceGet = customerDAO.getBalance(customerToBeSet.getNickname(), testTable);
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

        customerDAO.addCustomer(customerToBeSet, testTable);
        double balanceToWithdraw = 200.00;
        customerDAO.withdrawBalance(customerToBeSet.getNickname(), balanceToWithdraw, testTable);
    }

    @Test
    public void shouldDepositNewBalanceSuccessfully() throws DuplicateCustomerException, NicknameInvalidException {
        Customer customerToBeSet = new Customer();
        customerToBeSet.setNickname("adam");
        customerToBeSet.setDateOfBirth(new Date(Date.valueOf("1983-10-25").getTime()));
        customerToBeSet.setBalance(100.00);

        customerDAO.addCustomer(customerToBeSet, testTable);

        double balanceToDeposit = 23.00;
        customerDAO.deposit(customerToBeSet.getNickname(), balanceToDeposit, testTable);

        double expectedBalance = 123.00;
        assertThat(customerDAO.getBalance(customerToBeSet.getNickname(), testTable), is(expectedBalance));
    }
}
