package bank.icbc.database.dao;

import bank.icbc.domain.Customer;
import bank.icbc.exception.NicknameInvalidException;

import javax.sql.DataSource;
import java.sql.Date;

public interface CustomerDAO {
    public void setDataSource(DataSource ds);

    public void addCustomer(String name, Date dateOfBirth) throws NicknameInvalidException;

    public Customer getCustomer(String id);

    public void removeCustomer(Integer id);

}
