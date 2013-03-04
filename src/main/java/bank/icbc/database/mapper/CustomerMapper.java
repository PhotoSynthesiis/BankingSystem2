package bank.icbc.database.mapper;

import bank.icbc.domain.Customer;
import bank.icbc.exception.NicknameInvalidException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerMapper implements RowMapper<Customer> {
    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Customer customer = new Customer();
        try {
            customer.setNickname(rs.getString(1));
        } catch (NicknameInvalidException e) {
            e.printStackTrace();
        }
        customer.setDateOfBirth(rs.getDate(2));
        customer.setBalance(rs.getDouble(3));
        return customer;
    }
}
