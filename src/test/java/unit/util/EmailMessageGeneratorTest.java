package unit.util;

import bank.icbc.domain.Customer;
import bank.icbc.exception.CustomerException;
import bank.icbc.util.EmailMessageGenerator;
import org.junit.Test;
import org.springframework.mail.SimpleMailMessage;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EmailMessageGeneratorTest {
    @Test
    public void shouldFormCorrectEmailMessageForUserNotice() throws CustomerException {
        Customer customer = new Customer();
        customer.setNickname("nick");
        customer.setEmailAddress("nick@thebank.com");

        SimpleMailMessage message = EmailMessageGenerator.buildEmailMessageSendToCustomerAfterRegistration(customer);
        assertThat(message.getFrom(), is("admin@thebank.com"));
        assertThat(message.getText(), is("Dear nick, welcome to the bank"));
        assertThat(message.getSubject(), is("Welcome!"));
        assertThat(message.getTo()[0], is("nick@thebank.com"));
    }

    @Test
    public void shouldFormCorrectEmailMessageForManagerNotice() throws CustomerException {
        SimpleMailMessage message = EmailMessageGenerator.buildEmailMessageSendToManagerAfterCustomerBecomePremium("nick");

        assertThat(message.getFrom(), is("admin@thebank.com"));
        assertThat(message.getText(), is("nick is now a premium customer"));
        assertThat(message.getSubject(), is("Notice"));
        assertThat(message.getTo()[0], is("qsli@thoughtworks.com"));
    }
}
