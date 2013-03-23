package integration.domain;

import bank.icbc.domain.Bank;
import bank.icbc.domain.Customer;
import bank.icbc.domain.CustomerService;
import bank.icbc.exception.BalanceOverdrawException;
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
import java.util.List;

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

    private Customer customer;

    @Before
    public void setUp() throws CustomerException {
        setUpWiserServer();
        addCustomer();
    }

    private void setUpWiserServer() {
        wiser = new Wiser();
        wiser.setPort(25000);
        wiser.start();
    }

    private void addCustomer() throws CustomerException {
        String nickname = "dan";
        Date dateOfBirth = new Date(Date.valueOf("1988-09-03").getTime());
        int balance = 100;
        String emailAddress = "dan@test.com";
        boolean isPremium = false;

        customer = new Customer(nickname, dateOfBirth, balance, emailAddress, isPremium);
        bank.addCustomer(customer);
    }

    @After
    public void tearDown() {
        wiser.stop();
    }

    @Test
    @Transactional
    @Rollback(true)
    public void should_deposit_new_balance_successfully() throws CustomerException, BalanceOverdrawException {
        double balanceToDeposit = 23.00;
        service.deposit(customer.getNickname(), balanceToDeposit);

        double expectedBalance = 123.00;
        double balance = service.getBalance(customer.getNickname());

        assertThat(balance, is(expectedBalance));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void should_withdraw_successfully() throws CustomerException, BalanceOverdrawException {
        double balanceToWithdraw = 50.00;
        service.withdraw(customer.getNickname(), balanceToWithdraw);

        double balance = service.getBalance(customer.getNickname());
        double expectedBalance = 50.00;

        assertThat(balance, is(expectedBalance));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void should_withdraw_all_money_in_account_successfully() throws CustomerException, BalanceOverdrawException {
        double balanceToWithdraw = 100.00;
        service.withdraw(customer.getNickname(), balanceToWithdraw);

        double balance = service.getBalance(customer.getNickname());
        double expectedBalance = 0.00;

        assertThat(balance, is(expectedBalance));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void should_throw_exception_when_overdraw() throws CustomerException, BalanceOverdrawException {
        expectedException.expect(BalanceOverdrawException.class);

        double balanceToWithdraw = 400.00;
        service.withdraw(customer.getNickname(), balanceToWithdraw);
    }


    @Test
    @Transactional
    @Rollback(true)
    public void should_not_become_premium_user_when_balance_never_over_40000() throws BalanceOverdrawException, CustomerException {
        int balanceToDeposit = 39899;
        service.deposit(customer.getNickname(), balanceToDeposit);

        boolean isNonePremiumCustomer = false;
        Customer nonPremiumCustomer = bank.getCustomer(customer.getNickname());

        assertThat(nonPremiumCustomer.isPremium(), is(isNonePremiumCustomer));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void should_become_premium_user_when_balance_is_over_40000() throws BalanceOverdrawException, CustomerException {
        int balanceToDeposit = 40000;
        service.deposit(customer.getNickname(), balanceToDeposit);

        boolean isPremiumCustomer = true;
        Customer premiumCustomer = bank.getCustomer(customer.getNickname());

        assertThat(premiumCustomer.isPremium(), is(isPremiumCustomer));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void should_always_be_premium_customer_if_once_become_a_premium_customer_and_withdraw_some_money()
            throws BalanceOverdrawException, CustomerException {

        int balanceToDeposit = 40000;
        service.deposit(customer.getNickname(), balanceToDeposit);

        int balanceToWithdraw = 30000;
        service.withdraw(customer.getNickname(), balanceToWithdraw);

        boolean isPremiumCustomer = true;
        Customer premiumCustomer = bank.getCustomer(customer.getNickname());

        assertThat(premiumCustomer.isPremium(), is(isPremiumCustomer));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void should_always_be_premium_customer_if_once_become_a_premium_customer_and_withdraw_all_money()
            throws BalanceOverdrawException, CustomerException {
        int balanceToDeposit = 40000;
        service.deposit(customer.getNickname(), balanceToDeposit);

        int balanceToWithdraw = 40100;
        service.withdraw(customer.getNickname(), balanceToWithdraw);

        boolean isPremiumCustomer = true;
        Customer premiumCustomer = bank.getCustomer(customer.getNickname());

        assertThat(premiumCustomer.isPremium(), is(isPremiumCustomer));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void should_send_email_to_manager_once_customer_become_premium() throws BalanceOverdrawException {
        int balanceToDeposit = 40000;
        service.deposit(customer.getNickname(), balanceToDeposit);

        WiserMessage message = getEmailSentToManager();

        assertThat(message.getEnvelopeSender(), is("admin@thebank.com"));
        assertThat(message.getEnvelopeReceiver(), is("manager@thebank.com"));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void should_not_send_customer_become_premium_email_to_customer_more_than_once() throws BalanceOverdrawException {
        int balanceToDeposit = 40000;
        service.deposit(customer.getNickname(), balanceToDeposit);
        service.deposit(customer.getNickname(), balanceToDeposit);

        List<WiserMessage> messages = wiser.getMessages();

        assertThat(messages.size(), is(2));
    }

    private WiserMessage getEmailSentToManager() {
        return wiser.getMessages().get(1);
    }

}
