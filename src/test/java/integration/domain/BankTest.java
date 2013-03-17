package integration.domain;

import bank.icbc.domain.Bank;
import bank.icbc.domain.Customer;
import bank.icbc.exception.*;
import org.junit.BeforeClass;
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

    private static Customer customer;

    @BeforeClass
    public static void beforeClass() throws NicknameInvalidException, DateOfBirthInvalidException {
        customer = new Customer("dan", new Date(Date.valueOf("1982-10-12").getTime()), 100.00);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void should_add_customer_successfully() throws NicknameInvalidException, CustomerNotFoundException, DuplicateCustomerException, DateOfBirthInvalidException {
        bank.addCustomer(customer);

        Customer customerGet = bank.getCustomer("dan");
        assertThat("dan", is(customerGet.getNickname()));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void should_throw_exception_when_add_customer_with_same_nickname() throws NicknameInvalidException, DuplicateCustomerException, DateOfBirthInvalidException {
        expectedException.expect(DuplicateCustomerException.class);

        bank.addCustomer(customer);
        bank.addCustomer(customer);
    }
}
