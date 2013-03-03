package bank.icbc.database.service;

import bank.icbc.database.template.CustomerDAO;
import bank.icbc.domain.Customer;
import bank.icbc.exception.CustomerNotFoundException;
import bank.icbc.exception.DuplicateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("customerService")
public class CustomerService {

    @Autowired
    @Qualifier("customerDAO")
    private CustomerDAO customerDAO;

    private final String defaultTableNameForCustomer = "customer";
    private String tableName = defaultTableNameForCustomer;

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Transactional
    public void addCustomer(Customer customer) throws DuplicateCustomerException {
        customerDAO.addCustomer(customer, tableName);
    }

    public Customer getCustomers(String nickname) throws CustomerNotFoundException {
        return customerDAO.getCustomer(nickname, tableName);
    }
}
