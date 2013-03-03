package bank.icbc.database.template;

import bank.icbc.database.mapper.CustomerMapper;
import bank.icbc.domain.Customer;
import bank.icbc.exception.BalanceOverdrawException;
import bank.icbc.exception.CustomerNotFoundException;
import bank.icbc.exception.DuplicateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository("customerDAO")
public class CustomerDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createTable(String tableName) {
        jdbcTemplate.execute("DROP TABLE IF EXISTS " + tableName);
        jdbcTemplate.execute("CREATE TABLE " + tableName + "(" + "nickname VARCHAR(45) NOT NULL ," +
                "dateOfBirth DATETIME NOT NULL," + "balance DOUBLE NOT NULL, " + "PRIMARY KEY(nickname)" + ");");
    }

    public void addCustomer(Customer customer, String tableName) throws DuplicateCustomerException {
        String SQL = "insert into " + tableName + "(nickname, dateOfBirth, balance) values (?, ?, ?)";
        try {
            jdbcTemplate.update(SQL, new Object[]{customer.getNickname(), customer.getDateOfBirth(), customer.getBalance()});
        } catch (DuplicateKeyException exception) {
            throw new DuplicateCustomerException("Customer with nickname " + customer.getNickname() + " has already existed");
        }
    }

    public Customer getCustomer(String nickname, String tableName) throws CustomerNotFoundException {
        String SQL = "select * from " + tableName + " where nickname = ?";
        Customer customer;
        try {
            customer = jdbcTemplate.queryForObject(SQL, new Object[]{nickname}, new CustomerMapper());
        } catch (EmptyResultDataAccessException exception) {
            throw new CustomerNotFoundException("Customer with nickname " + nickname + " is not found");
        }
        return customer;
    }

    public Double getBalance(String nickname, String tableName) {
        String SQL = "select balance from " + tableName + " where nickname = ?";
        double balance = jdbcTemplate.queryForObject(SQL, new Object[]{nickname}, Double.class);
        return balance;
    }

    public void withdrawBalance(String nickname, double balanceToWithdraw, String tableName) throws BalanceOverdrawException {
        double balanceBefore = getBalance(nickname, tableName);
        double balanceAfter = balanceBefore - balanceToWithdraw;

        if (balanceAfter < 0) {
            throw new BalanceOverdrawException("You have only " + balanceBefore + "$. You can not withdraw " + balanceToWithdraw + "$");
        }

        String SQL = "update " + tableName + " set balance = ? where nickname = ?";

        jdbcTemplate.update(SQL, new Object[]{balanceAfter, nickname});
    }

    public void addBalance(String nickname, double balanceToDeposit, String tableName) {
        double balanceBefore = getBalance(nickname, tableName);
        double balanceAfter = balanceBefore + balanceToDeposit;

        String SQL = "update " + tableName + " set balance = ? where nickname = ?";
        jdbcTemplate.update(SQL, new Object[]{balanceAfter, nickname});
    }
}
