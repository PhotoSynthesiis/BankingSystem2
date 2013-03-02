package bank.icbc.database.unit.controller;

import bank.icbc.database.template.CustomerDAO;
import bank.icbc.domain.Customer;
import bank.icbc.exception.DuplicateCustomerException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.sql.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContextDataSource.xml",
        "file:src/main/webapp/WEB-INF/applicationContext-servlet.xml"})
public class CustomerControllerTest {
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Autowired
    private RequestMappingHandlerMapping mappingHandler;

    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    @Autowired
    @Qualifier("customerDAO")
    private CustomerDAO customerDAO;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        customerDAO.createTable("customer");
    }

    @After
    public void tearDown() {
        customerDAO.deleteTable("customer");
    }

    @Test
    public void shouldReturnAddCustomerAsView() throws Exception {
        request.setRequestURI("/addCustomer");
        request.setMethod(HttpMethod.GET.toString());
        Object controller = mappingHandler.getHandler(request).getHandler();

        ModelAndView modelAndView = handlerAdapter.handle(request, response, controller);

        assertThat("AddCustomer", is(modelAndView.getViewName()));
    }

    @Test
    public void shouldReturnShowCustomerAsView() throws Exception {
        Customer customer = new Customer();
        customer.setNickname("dan");
        customer.setDateOfBirth(new Date(Date.valueOf("1982-10-02").getTime()));

        request.setParameter("nickname", "dan");
        request.setParameter("dateOfBirth", "1980-09-01");

        request.setRequestURI("/addCustomer");
        request.setMethod(HttpMethod.POST.toString());

        Object controller = mappingHandler.getHandler(request).getHandler();

        ModelAndView modelAndView = handlerAdapter.handle(request, response, controller);

        assertThat("ShowCustomer", is(modelAndView.getViewName()));
    }
}
