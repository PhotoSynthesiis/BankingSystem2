package bank.icbc.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("bank")
public class Bank {

    @Autowired
    @Qualifier("bankService")
    private BankService bankService;

    public void addCustomer(Customer customer) {
        bankService.addCustomer(customer);
    }

    public Customer getCustomer(String nickname) {
        return bankService.getCustomer(nickname);
    }
}
