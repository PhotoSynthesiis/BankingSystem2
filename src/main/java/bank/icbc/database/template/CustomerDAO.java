package bank.icbc.database.template;

import bank.icbc.database.mapper.CustomerMapper;
import bank.icbc.domain.Customer;
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
        jdbcTemplate.execute("CREATE TABLE " + tableName + "(" + "nickname VARCHAR(45) Not NULL ," +
                "dateOfBirth DATETIME Not NULL," + "PRIMARY KEY(nickname)" + ");");
    }

    public void addCustomer(Customer customer) throws DuplicateCustomerException {
        String SQL = "insert into customer (nickname, dateOfBirth) values (?, ?)";
        try {
            jdbcTemplate.update(SQL, new Object[]{customer.getNickname(), customer.getDateOfBirth()});
        } catch (DuplicateKeyException exception) {
            throw new DuplicateCustomerException();
        }
    }

    public Customer getCustomer(String nickname) throws CustomerNotFoundException {
        String SQL = "select * from customer where nickname = ?";
        Customer customer;
        try {
            customer = jdbcTemplate.queryForObject(SQL, new Object[]{nickname}, new CustomerMapper());
        } catch (EmptyResultDataAccessException exception) {
            throw new CustomerNotFoundException();
        }
        return customer;
    }

    public void deleteTable(String table) {
        jdbcTemplate.execute("DROP TABLE " + table);
    }
}
