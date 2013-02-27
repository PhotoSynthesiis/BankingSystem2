package bank.icbc.database.template;

import bank.icbc.domain.Customer;
import bank.icbc.exception.NicknameInvalidException;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import testPreparation.CustomerFixture;

import java.sql.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CustomerJDBCTemplateTest extends CustomerFixture {
    private static ApplicationContext context;

    private static CustomerJDBCTemplate customerJDBCTemplate;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @BeforeClass
    public static void beforeClass() {
        context = new ClassPathXmlApplicationContext("dataSource.xml");
        customerJDBCTemplate = (CustomerJDBCTemplate) context.getBean("customerJDBCTemplate");
        setCustomerJDBCTemplate(customerJDBCTemplate);
    }

    @Test
    public void shouldThrowExceptionWhenAddCustomerWithoutNickname() throws NicknameInvalidException {
        expectedException.expect(NicknameInvalidException.class);
        String nickName = "";
        Date dateOfBirth = new Date(Date.valueOf("1983-10-25").getTime());
        customerJDBCTemplate.addCustomer(nickName, dateOfBirth);
    }

    @Test
    public void shouldThrowExceptionWhenAddCustomerWithNicknameContainsOnlySpaces() throws NicknameInvalidException {
        expectedException.expect(NicknameInvalidException.class);
        String nickName = "       ";
        Date dateOfBirth = new Date(Date.valueOf("1983-10-25").getTime());
        customerJDBCTemplate.addCustomer(nickName, dateOfBirth);
    }

    @Test
    public void shouldThrowExceptionWhenAddCustomerWithInvalidNickname() throws NicknameInvalidException {
        expectedException.expect(NicknameInvalidException.class);
        String nickName = "A2s";
        Date dateOfBirth = new Date(Date.valueOf("1983-10-25").getTime());
        customerJDBCTemplate.addCustomer(nickName, dateOfBirth);
    }

    @Test
    public void shouldAddCustomerSuccessfully() throws NicknameInvalidException {
        String nickName = "adam";
        Date dateOfBirth = new Date(Date.valueOf("1983-10-25").getTime());
        customerJDBCTemplate.addCustomer(nickName, dateOfBirth);

        Customer customer = customerJDBCTemplate.getCustomer(nickName);

        assertThat(nickName, is(customer.getNickname()));
    }

    @Test
    public void shouldThrowExceptionWhenAddTwoCustomersWithSameNickname() throws NicknameInvalidException {
        expectedException.expect(DuplicateKeyException.class);
        String nickName1 = "adam";
        String nickName2 = "adam";
        Date dateOfBirth1 = new Date(Date.valueOf("1983-10-25").getTime());
        Date dateOfBirth2 = new Date(Date.valueOf("1983-10-25").getTime());

        customerJDBCTemplate.addCustomer(nickName1, dateOfBirth1);
        customerJDBCTemplate.addCustomer(nickName2, dateOfBirth2);
    }
}
