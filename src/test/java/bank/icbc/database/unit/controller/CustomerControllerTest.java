package bank.icbc.database.unit.controller;

import bank.icbc.database.service.CustomerService;
import bank.icbc.database.template.CustomerDAO;
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
    CustomerService service;

    @Autowired
    @Qualifier("customerDAO")
    private CustomerDAO customerDAO;

    @Rule
    public ExpectedException exception = ExpectedException.none();
    private String testCustomer = "testCustomer";

    @Before
    public void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        customerDAO.createTable(testCustomer);
        service.setTableName(testCustomer);
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
        request.setParameter("nickname", "dan");
        request.setParameter("dateOfBirth", "1980-09-01");

        request.setRequestURI("/addCustomer");
        request.setMethod(HttpMethod.POST.toString());

        Object controller = mappingHandler.getHandler(request).getHandler();

        ModelAndView modelAndView = handlerAdapter.handle(request, response, controller);

        assertThat("ShowCustomer", is(modelAndView.getViewName()));
    }

//    @Test
//    public void shouldReturnExceptionAsView() throws Exception {
//        request.setParameter("nickname", "dan");
//        request.setParameter("dateOfBirth", "1980-09-01");
//
//        request.setRequestURI("/addCustomer");
//        request.setMethod(HttpMethod.POST.toString());
//
//        Object controller = mappingHandler.getHandler(request).getHandler();
//
//        handlerAdapter.handle(request, response, controller);
//        ModelAndView modelAndView = handlerAdapter.handle(request, response, controller);
//
//        assertThat("exception", is(modelAndView.getViewName()));
//    }

    @Test
    public void shouldReturnWelcomeAsView() throws Exception {
        request.setRequestURI("/welcome");
        request.setMethod(HttpMethod.GET.toString());
        Object controller = mappingHandler.getHandler(request).getHandler();

        ModelAndView modelAndView = handlerAdapter.handle(request, response, controller);

        assertThat("Welcome", is(modelAndView.getViewName()));
    }

    @Test
    public void shouldReturnWithdrawAsView() throws Exception {
        request.setRequestURI("/withdraw");
        request.setMethod(HttpMethod.GET.toString());
        Object controller = mappingHandler.getHandler(request).getHandler();

        ModelAndView modelAndView = handlerAdapter.handle(request, response, controller);

        assertThat("Withdraw", is(modelAndView.getViewName()));
    }
}
