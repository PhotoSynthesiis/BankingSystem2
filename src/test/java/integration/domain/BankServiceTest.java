package integration.domain;

import bank.icbc.domain.BankService;
import bank.icbc.domain.Customer;
import bank.icbc.exception.CustomerException;
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
import org.subethamail.wiser.WiserMessage;

import java.sql.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContextDataSource-test.xml",
        "classpath:applicationContext-servlet-test.xml"})
public class BankServiceTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    @Qualifier("bankService")
    private BankService bankService;

    private Customer customer;
    private Wiser wiser;

    private static final String nickname = "dan";
    private static final Date dateOfBirth = new Date(Date.valueOf("1982-10-12").getTime());
    private static final double balance = 100.00;
    private static final String emailAddress = "abc@test.com";
    private static final boolean isPremium = false;
    private static final Date joinDate = new Date(Date.valueOf("1988-04-03").getTime());

    @Before
    public void setUp() throws CustomerException {
        wiser = new Wiser();
        wiser.setPort(25000);
        wiser.start();

        customer = new Customer(nickname, dateOfBirth, balance, emailAddress, isPremium, joinDate);
    }

    @After
    public void tearDown() {
        wiser.stop();
    }

    @Test
    @Transactional
    @Rollback(true)
    public void should_add_customer_successfully() throws CustomerException {
        bankService.addCustomer(customer);

        Customer customerGet = bankService.getCustomer("dan");
        assertThat("dan", is(customerGet.getNickname()));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void should_throw_exception_when_add_customer_with_same_nickname() throws CustomerException {
        expectedException.expect(CustomerException.class);

        bankService.addCustomer(customer);
        bankService.addCustomer(customer);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void should_send_email_to_customer_after_registration() throws CustomerException {
        bankService.addCustomer(customer);

        WiserMessage message = wiser.getMessages().get(0);

        assertNotNull(message);
    }
}
