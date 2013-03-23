package bank.icbc.database.mapper;

import bank.icbc.domain.CustomerStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerStatusMapper implements RowMapper<CustomerStatus> {
    @Override
    public CustomerStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
        String nickname = rs.getString(1);
        boolean isEmailToManagerSent = rs.getBoolean(2);
        CustomerStatus status = new CustomerStatus(nickname, isEmailToManagerSent);
        return status;
    }
}
