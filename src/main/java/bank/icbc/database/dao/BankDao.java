package bank.icbc.database.dao;

import bank.icbc.database.mapper.CustomerStatusMapper;
import bank.icbc.domain.CustomerStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service("bankDao")
public class BankDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public CustomerStatus get(String nickname) {
        String SQL = "select * from customerStatus where nickname = ?";
        return jdbcTemplate.queryForObject(SQL, new Object[]{nickname}, new CustomerStatusMapper());
    }

    public void update(CustomerStatus status) {
        String SQL = "update customerStatus set emailToCustomerHasSent = ? where nickname = ?";
        jdbcTemplate.update(SQL, new Object[]{status.isEmailToManagerSent(), status.getNickname()});
    }

    public void add(CustomerStatus status) {
        String SQL = "insert into customerStatus (nickname, emailToCustomerHasSent) values (?, ?)";
        jdbcTemplate.update(SQL, new Object[]{status.getNickname(), status.isEmailToManagerSent()});
    }
}
