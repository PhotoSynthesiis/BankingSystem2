package bank.icbc.database.mapper;

import bank.icbc.domain.Customer;
import bank.icbc.exception.CustomerException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerMapper implements RowMapper<Customer> {
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        String nickname = rs.getString(1);
        Date dateOfBirth = rs.getDate(2);
        double balance = rs.getDouble(3);
        String emailAddress = rs.getString(4);
        boolean isPremium = rs.getBoolean(5);
        Date joinDate = rs.getDate(6);
        Customer customer = null;
        try {
            customer = new Customer(nickname, dateOfBirth, balance, emailAddress, isPremium, joinDate);
        } catch (CustomerException e) {
            e.printStackTrace();
        }
        return customer;
    }
}
