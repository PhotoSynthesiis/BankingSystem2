package bank.icbc.database.template;

import bank.icbc.database.dao.CustomerDAO;
import bank.icbc.domain.Customer;
import bank.icbc.domain.CustomerMapper;
import bank.icbc.exception.NicknameInvalidException;
import bank.icbc.validator.CustomerValidator;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Date;

public class CustomerJDBCTemplate implements CustomerDAO {
    private DataSource dataSource;


    private JdbcTemplate jdbcTemplate;

    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public void addCustomer(String nickname, Date dateOfBirth) throws NicknameInvalidException {
        if (!CustomerValidator.isNicknameValid(nickname)) {
            throw new NicknameInvalidException("Nickname should be combination of lower case of letters and digits (empty value or space is not allowed)");
        }
        String SQL = "insert into customer (nickname, dateOfBirth) values (?, ?)";
        jdbcTemplate.update(SQL, nickname, dateOfBirth);
    }

    @Override
    public Customer getCustomer(String nickname) {
        String SQL = "select * from customer where nickname = ?";
        Customer customer = jdbcTemplate.queryForObject(SQL, new Object[]{nickname}, new CustomerMapper());
        return customer;
    }

    @Override
    public void removeCustomer(Integer id) {
    }
}
