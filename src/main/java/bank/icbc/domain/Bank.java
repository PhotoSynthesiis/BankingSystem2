package bank.icbc.domain;

import bank.icbc.database.dao.CustomerDao;
import bank.icbc.exception.CustomerNotFoundException;
import bank.icbc.exception.DuplicateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service("bank")
public class Bank {

    @Autowired
    @Qualifier("customerDao")
    private CustomerDao customerDao;

    @Autowired
    private MailSender mailSender;

    public void addCustomer(Customer customer) throws DuplicateCustomerException {
        try {
            customerDao.add(customer);
        } catch (DuplicateKeyException exception) {
            throw new DuplicateCustomerException("Customer with nickname " + customer.getNickname() + " has already existed");
        }
        sendEmailToCustomer(customer);
    }

    private void sendEmailToCustomer(Customer customer) {
        mailSender.sendEmail(customer.getNickname(), customer.getEmailAddress());
    }

    public Customer getCustomer(String nickname) throws CustomerNotFoundException {
        try {
            return customerDao.get(nickname);
        } catch (EmptyResultDataAccessException exception) {
            throw new CustomerNotFoundException("Customer with nickname " + nickname + " is not found");
        }
    }
}
