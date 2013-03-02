package bank.icbc.database.integration;

import bank.icbc.database.template.CustomerDAO;
import bank.icbc.domain.Customer;
import bank.icbc.exception.CustomerNotFoundException;
import bank.icbc.exception.DuplicateCustomerException;
import bank.icbc.exception.NicknameInvalidException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/applicationContextDataSource.xml")
public class CustomerDAOTest {

    @Autowired
    @Qualifier("customerDAO")
    private CustomerDAO customerDAO;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void beforeClass() {
        customerDAO.createTable("customer");
    }

    @Test
    public void shouldAddCustomerSuccessfully() throws NicknameInvalidException, CustomerNotFoundException, DuplicateCustomerException {
        Customer customerToBeSet = new Customer();
        customerToBeSet.setNickname("adam");
        customerToBeSet.setDateOfBirth(new Date(Date.valueOf("1983-10-25").getTime()));

        customerDAO.addCustomer(customerToBeSet);

        Customer customerGet = customerDAO.getCustomer("adam");
        assertThat("adam", is(customerGet.getNickname()));
    }

    @Test
    public void shouldThrowExceptionWhenAddTwoCustomersWithSameNickname() throws NicknameInvalidException, DuplicateCustomerException {
        expectedException.expect(DuplicateKeyException.class);

        Customer customer1 = new Customer();
        customer1.setNickname("adam");
        customer1.setDateOfBirth(new Date(Date.valueOf("1983-10-25").getTime()));

        Customer customer2 = new Customer();
        customer2.setNickname("adam");
        customer2.setDateOfBirth(new Date(Date.valueOf("1983-10-25").getTime()));

        customerDAO.addCustomer(customer1);
        customerDAO.addCustomer(customer2);
    }

}
