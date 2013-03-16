package integration.domain;

import bank.icbc.domain.MailSender;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

import javax.mail.MessagingException;
import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-servlet-test.xml",
        "classpath:applicationContextDataSource.xml"})
public class MailSenderTest {
    private Wiser wiser;

    @Autowired
    private MailSender mailSender;

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
    public void should_send_email_successfully() throws MessagingException, IOException {

        String customerNickname = "adam";
        String customerEmail = "adam@test.com";
        mailSender.sendEmail(customerNickname, customerEmail);

        WiserMessage wiserMessage = wiser.getMessages().get(0);
        String expectedReceiver = "adam@test.com";
        String expectedSender = "admin@thebank.com";
        String expectedSubject = "Welcome!";
        String expectedContent = "Dear adam, welcome to the bank";

        assertThat(wiserMessage.getEnvelopeReceiver(), is(expectedReceiver));
        assertThat(wiserMessage.getEnvelopeSender(), is(expectedSender));
        assertThat(wiserMessage.getMimeMessage().getContent().toString().trim(), is(expectedContent));
        assertThat(wiserMessage.getMimeMessage().getSubject().trim(), is(expectedSubject));
    }
}
