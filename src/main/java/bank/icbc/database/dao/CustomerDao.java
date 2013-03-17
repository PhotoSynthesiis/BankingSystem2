package bank.icbc.database.dao;

import bank.icbc.database.mapper.CustomerMapper;
import bank.icbc.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Component
@Repository("customerDao")
public class CustomerDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void add(Customer customer) {
        String SQL = "insert into customer (nickname, dateOfBirth, balance) values (?, ?, ?)";
        jdbcTemplate.update(SQL, new Object[]{customer.getNickname(), customer.getDateOfBirth(), customer.getBalance()});
    }

    public Customer get(String nickname) {
        String SQL = "select * from customer where nickname = ?";
        return jdbcTemplate.queryForObject(SQL, new Object[]{nickname}, new CustomerMapper());
    }

    public void update(Customer customer) {
        String nickname = customer.getNickname();
        Date dateOfBirth = customer.getDateOfBirth();
        double balance = customer.getBalance();

        String SQL = "update customer set balance = ?, dateOfBirth = ? where nickname = ?";
        jdbcTemplate.update(SQL, new Object[]{balance, dateOfBirth, nickname});
    }
}
