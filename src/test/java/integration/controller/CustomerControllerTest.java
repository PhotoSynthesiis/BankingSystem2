package integration.controller;

import bank.icbc.database.dao.CustomerDao;
import bank.icbc.database.service.CustomerService;
import bank.icbc.domain.Customer;
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
@ContextConfiguration(locations = {"classpath:applicationContext-servlet.xml",
        "classpath:applicationContextDataSource-test.xml"})
public class CustomerControllerTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Autowired
    private RequestMappingHandlerMapping mappingHandler;

    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    @Autowired
    private CustomerService service;

    @Autowired
    @Qualifier("customerDao")
    private CustomerDao customerDao;

    @Rule
    public ExpectedException exception = ExpectedException.none();
    private String testTable = "customer";

    @Before
    public void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        customerDao.createTable(testTable);
    }

    @After
    public void tearDown() {
        customerDao.deleteTable(testTable);
    }

    @Test
    public void should_return_add_customer() throws Exception {
        request.setRequestURI("/addCustomer");
        request.setMethod(HttpMethod.GET.toString());
        Object controller = mappingHandler.getHandler(request).getHandler();

        ModelAndView modelAndView = handlerAdapter.handle(request, response, controller);

        assertThat("AddCustomer", is(modelAndView.getViewName()));
    }

    @Test
    public void should_return_show_customer() throws Exception {
        request.setParameter("nickname", "dan");
        request.setParameter("dateOfBirth", "1980-09-01");

        request.setRequestURI("/addCustomer");
        request.setMethod(HttpMethod.POST.toString());

        Object controller = mappingHandler.getHandler(request).getHandler();

        ModelAndView modelAndView = handlerAdapter.handle(request, response, controller);

        assertThat("ShowCustomer", is(modelAndView.getViewName()));
    }

    @Test
    public void should_return_welcome() throws Exception {
        request.setRequestURI("/welcome");
        request.setMethod(HttpMethod.GET.toString());
        Object controller = mappingHandler.getHandler(request).getHandler();

        ModelAndView modelAndView = handlerAdapter.handle(request, response, controller);

        assertThat("Welcome", is(modelAndView.getViewName()));
    }

    @Test
    public void should_return_withdraw() throws Exception {
        request.setRequestURI("/withdraw");
        request.setMethod(HttpMethod.GET.toString());
        Object controller = mappingHandler.getHandler(request).getHandler();

        ModelAndView modelAndView = handlerAdapter.handle(request, response, controller);

        assertThat("Withdraw", is(modelAndView.getViewName()));
    }

    @Test
    public void should_return_deposit() throws Exception {
        request.setRequestURI("/deposit");
        request.setMethod(HttpMethod.GET.toString());
        Object controller = mappingHandler.getHandler(request).getHandler();

        ModelAndView modelAndView = handlerAdapter.handle(request, response, controller);

        assertThat("Deposit", is(modelAndView.getViewName()));
    }

    @Test
    public void should_return_show_balance() throws Exception {
        Customer customer = new Customer();
        customer.setNickname("dan");
        customer.setDateOfBirth(new Date(Date.valueOf("1990-09-08").getTime()));
        customer.setBalance(12.00);

        service.addCustomer(customer);

        request.setParameter("nickname", "dan");
        request.setParameter("balance", "12.00");

        request.setRequestURI("/deposit");
        request.setMethod(HttpMethod.POST.toString());
        Object controller = mappingHandler.getHandler(request).getHandler();

        ModelAndView modelAndView = handlerAdapter.handle(request, response, controller);
        assertThat("ShowBalance", is(modelAndView.getViewName()));
    }
}
