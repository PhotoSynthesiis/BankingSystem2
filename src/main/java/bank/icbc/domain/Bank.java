package bank.icbc.domain;

import bank.icbc.exception.CustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("bank")
public class Bank {

    @Autowired
    @Qualifier("bankService")
    private BankService bankService;

    public void addCustomer(Customer customer) throws CustomerException {
        bankService.addCustomer(customer);
    }

    public Customer getCustomer(String nickname) throws CustomerException {
        return bankService.getCustomer(nickname);
    }
}
