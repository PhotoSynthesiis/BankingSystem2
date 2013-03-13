package bank.icbc.database.mapper;

import bank.icbc.domain.Customer;
import bank.icbc.exception.DateOfBirthInvalidException;
import bank.icbc.exception.NicknameInvalidException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerMapper implements RowMapper<Customer> {
    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        String nickname = rs.getString(1);
        Date dateOfBirth = rs.getDate(2);
        double balance = rs.getDouble(3);
        Customer customer = null;
        try {
            customer = new Customer(nickname, dateOfBirth, balance);
        } catch (NicknameInvalidException e) {
            e.printStackTrace();
        } catch (DateOfBirthInvalidException e) {
            e.printStackTrace();
        }
        return customer;
    }
}
