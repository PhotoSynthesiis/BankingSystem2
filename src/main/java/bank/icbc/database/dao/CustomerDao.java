package bank.icbc.database.dao;

import bank.icbc.database.mapper.CustomerMapper;
import bank.icbc.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository("customerDao")
public class CustomerDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createTable(String tableName) {
        String SQL_DROP = "DROP TABLE IF EXISTS " + tableName;
        String SQL_CREATE = "CREATE TABLE " + tableName + "(" + "nickname VARCHAR(45) NOT NULL ," +
                "dateOfBirth DATETIME NOT NULL," + "balance DOUBLE NOT NULL, " + "PRIMARY KEY(nickname)" + ");";

        jdbcTemplate.execute(SQL_DROP);
        jdbcTemplate.execute(SQL_CREATE);
    }

    public void deleteTable(String tableName) {
        jdbcTemplate.execute("DROP TABLE IF EXISTS " + tableName);
    }

    public void addCustomer(Customer customer) {
        String SQL = "insert into customer (nickname, dateOfBirth, balance) values (?, ?, ?)";
        jdbcTemplate.update(SQL, new Object[]{customer.getNickname(), customer.getDateOfBirth(), customer.getBalance()});
    }

    public Customer getCustomer(String nickname) {
        String SQL = "select * from customer where nickname = ?";
        return jdbcTemplate.queryForObject(SQL, new Object[]{nickname}, new CustomerMapper());
    }

    public Double getBalance(String nickname) {
        String SQL = "select balance from customer where nickname = ?";
        return jdbcTemplate.queryForObject(SQL, new Object[]{nickname}, Double.class);
    }

    public void handleBalance(String nickname, double balance) {
        String SQL = "update customer set balance = ? where nickname = ?";
        jdbcTemplate.update(SQL, new Object[]{balance, nickname});
    }
}