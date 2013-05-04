package unit.util;

import bank.icbc.domain.CustomMailMessage;
import bank.icbc.domain.Customer;
import bank.icbc.util.EmailMessageBuilder;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EmailMessageGeneratorTest {
    @Test
    public void shouldFormCorrectEmailMessageForUserNotice() {
        Customer customer = new Customer();
        customer.setNickname("nick");
        customer.setEmailAddress("nick@thebank.com");

        CustomMailMessage message = EmailMessageBuilder.buildEmailMessageSendToCustomerAfterRegistration(customer);
        assertThat(message.getFromAddress(), is("admin@thebank.com"));
        assertThat(message.getEmailContent(), is("Dear nick, welcome to the bank"));
        assertThat(message.getEmailSubject(), is("Welcome!"));
        assertThat(message.getToAddress(), is("nick@thebank.com"));
    }

    @Test
    public void shouldFormCorrectEmailMessageForManagerNotice() {
        CustomMailMessage message = EmailMessageBuilder.buildEmailMessageSendToManagerAfterCustomerBecomePremium("nick");

        assertThat(message.getFromAddress(), is("admin@thebank.com"));
        assertThat(message.getEmailContent(), is("nick is now a premium customer"));
        assertThat(message.getEmailSubject(), is("Notice"));
        assertThat(message.getToAddress(), is("qsli@thoughtworks.com"));
    }
}
