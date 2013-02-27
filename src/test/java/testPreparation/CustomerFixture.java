package testPreparation;

import bank.icbc.database.template.CustomerJDBCTemplate;
import org.junit.AfterClass;
import org.junit.Before;

import javax.sql.DataSource;
import java.sql.*;

public class CustomerFixture {

    private static CustomerJDBCTemplate customerJDBCTemplate;
    private DataSource dataSource = customerJDBCTemplate.getDataSource();

    @Before
    public void setUp() throws SQLException {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        statement.execute("DROP TABLE IF EXISTS customer");
        statement.execute("CREATE TABLE customer (" + "nickname VARCHAR(45) Not NULL ," + "dateOfBirth DATETIME Not NULL," + "PRIMARY KEY(nickname)" + ");");

        statement.close();
        connection.close();
    }

    @AfterClass
    public static void afterClass() {

    }

    protected static void setCustomerJDBCTemplate(CustomerJDBCTemplate customerJDBCTemplate) {
        CustomerFixture.customerJDBCTemplate = customerJDBCTemplate;
    }
}
