package bank.icbc.database.dao;

import bank.icbc.database.mapper.CustomerMapper;
import bank.icbc.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service("customerDao")
public class CustomerDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void add(Customer customer) {
        String SQL = "insert into customer (nickname, dateOfBirth, balance, emailAddress, isPremium) values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(SQL, new Object[]{customer.getNickname(), customer.getDateOfBirth(),
                customer.getBalance(), customer.getEmailAddress(), customer.isPremium()});
    }

    public Customer get(String nickname) {
        String SQL = "select * from customer where nickname = ?";
        return jdbcTemplate.queryForObject(SQL, new Object[]{nickname}, new CustomerMapper());
    }

    public void update(Customer customer) {
        String SQL = "update customer set dateOfBirth = ?, balance = ?, emailAddress = ?, isPremium = ? where nickname = ?";
        jdbcTemplate.update(SQL, new Object[]{customer.getDateOfBirth(),
                customer.getBalance(), customer.getEmailAddress(), customer.isPremium(), customer.getNickname()});
    }
}
