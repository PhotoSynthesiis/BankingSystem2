package integration.controller;

import bank.icbc.controller.CustomerController;
import bank.icbc.domain.Bank;
import bank.icbc.domain.Customer;
import bank.icbc.exception.CustomerException;
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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.subethamail.wiser.Wiser;

import java.sql.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-servlet-test.xml",
        "classpath:applicationContextDataSource-test.xml"})
public class CustomerControllerTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private Wiser wiser;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private RequestMappingHandlerMapping mappingHandler;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    @Autowired
    @Qualifier("bank")
    private Bank bank;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private CustomerController customerController;

    private Customer customer;

    @Before
    public void setUp() throws CustomerException {
        wiser = new Wiser();
        wiser.setPort(25000);
        wiser.start();

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

        customer = new Customer("dan", new Date(Date.valueOf("1982-10-12").getTime()), 100.00, "abc@test.com", false);

        customerController = new CustomerController();
    }

    @After
    public void tearDown() {
        wiser.stop();
    }

    @Test
    public void should_return_add_customer() throws Exception {
        assertThat(customerController.addCustomer(null), is("AddCustomer"));
    }

    public void should_return_welcome() throws Exception {
        assertThat(customerController.backToWelcomePage(), is("Welcome"));
    }

    public void should_return_withdraw() throws Exception {
        assertThat(customerController.goToWithdrawPage(null), is("Withdraw"));
    }

    public void should_return_deposit() throws Exception {
        assertThat(customerController.goToDepositPage(null), is("Deposit"));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void should_return_show_customer() throws Exception {
        request.setParameter("nickname", "dan");
        request.setParameter("dateOfBirth", "1980-09-01");
        request.setParameter("emailAddress", "abc@test.com");
        request.setRequestURI("/addCustomer");
        request.setMethod(HttpMethod.POST.toString());

        Object controller = mappingHandler.getHandler(request).getHandler();
        ModelAndView modelAndView = handlerAdapter.handle(request, response, controller);

        assertThat("ShowCustomer", is(modelAndView.getViewName()));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void should_return_show_balance() throws Exception {
        bank.addCustomer(customer);

        request.setParameter("nickname", "dan");
        request.setParameter("balance", "12.00");
        request.setRequestURI("/deposit");
        request.setMethod(HttpMethod.POST.toString());

        Object controller = mappingHandler.getHandler(request).getHandler();
        ModelAndView modelAndView = handlerAdapter.handle(request, response, controller);


        assertThat("ShowBalance", is(modelAndView.getViewName()));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void should_return_show_balance_test() throws Exception {
        bank.addCustomer(customer);

        request.setParameter("nickname", "dan");
        request.setParameter("balance", "12.00");
        request.setRequestURI("/withdraw");
        request.setMethod(HttpMethod.POST.toString());

        Object controller = mappingHandler.getHandler(request).getHandler();
        ModelAndView modelAndView = handlerAdapter.handle(request, response, controller);

        assertThat("ShowBalance", is(modelAndView.getViewName()));
    }
}
