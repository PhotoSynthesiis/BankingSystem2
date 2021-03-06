package bank.icbc.domain;

import bank.icbc.common.MailSender;
import bank.icbc.database.dao.CustomerDao;
import bank.icbc.exception.CustomerException;
import bank.icbc.util.EmailMessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service("bankService")
public class BankService {

    @Autowired
    @Qualifier("customerDao")
    private CustomerDao customerDao;

    @Autowired
    @Qualifier("mailSender")
    private MailSender mailSender;

    public void addCustomer(Customer customer) {
        try {
            customer.setJoinDate(new Date(System.currentTimeMillis()));
            customerDao.add(customer);
        } catch (DuplicateKeyException exception) {
            throw new CustomerException("Customer with nickname " + customer.getNickname() + " has already existed");
        }

        sendEmailToCustomer(customer);
    }

    public Customer getCustomer(String nickname) {
        try {
            return customerDao.get(nickname);
        } catch (EmptyResultDataAccessException exception) {
            throw new CustomerException("Customer with nickname " + nickname + " is not found");
        }
    }

    private void sendEmailToCustomer(Customer customer) {
        mailSender.sendEmail(EmailMessageBuilder.buildEmailMessageSendToCustomerAfterRegistration(customer));
    }
}
