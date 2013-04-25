package bank.icbc.domain;

import bank.icbc.database.dao.BankDao;
import bank.icbc.database.dao.CustomerDao;
import bank.icbc.exception.CustomerException;
import bank.icbc.util.EmailMessageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service("bankService")
public class BankService {

    @Autowired
    @Qualifier("customerDao")
    private CustomerDao customerDao;

    @Autowired
    @Qualifier("bankDao")
    private BankDao bankDao;

    @Autowired
    @Qualifier("mailSender")
    private MailSender mailSender;

    public void addCustomer(Customer customer) throws CustomerException {
        try {
            customerDao.add(customer);
        } catch (DuplicateKeyException exception) {
            throw new CustomerException("Customer with nickname " + customer.getNickname() + " has already existed");
        }

        setCustomerInitialStatus(customer);
        sendEmailToCustomer(customer);
    }

    private void setCustomerInitialStatus(Customer customer) {
        CustomerStatus status = new CustomerStatus(customer.getNickname(), false);
        bankDao.add(status);
    }

    public Customer getCustomer(String nickname) throws CustomerException {
        try {
            return customerDao.get(nickname);
        } catch (EmptyResultDataAccessException exception) {
            throw new CustomerException("Customer with nickname " + nickname + " is not found");
        }
    }

    private void sendEmailToCustomer(Customer customer) {
        mailSender.sendEmail(EmailMessageGenerator.buildEmailMessageSendToCustomerAfterRegistration(customer));
    }

    public boolean isPremiumEmailHasSentToCustomer(String nickname) {
        CustomerStatus status = getCustomerStatusFor(nickname);
        return status.isEmailToManagerSent();
    }

    public void emailToManagerHasBeenSent(String nickname) {
        CustomerStatus status = getCustomerStatusFor(nickname);
        status.setEmailToManagerSent(true);
        bankDao.update(status);
    }

    public CustomerStatus getCustomerStatusFor(String nickname) {
        CustomerStatus status = bankDao.get(nickname);
        return status;
    }
}
