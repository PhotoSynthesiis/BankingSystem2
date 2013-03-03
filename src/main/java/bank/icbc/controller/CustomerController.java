package bank.icbc.controller;

import bank.icbc.database.service.CustomerService;
import bank.icbc.domain.Customer;
import bank.icbc.exception.CustomerNotFoundException;
import bank.icbc.exception.DuplicateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CustomerController {

    @Autowired
    CustomerService service;

    @RequestMapping(value = "/addCustomer", method = RequestMethod.GET)
    public String addCustomer(@ModelAttribute Customer customer) {
        return "AddCustomer";
    }

    @RequestMapping(value = "/addCustomer", method = RequestMethod.POST)
    public String saveCustomer(@ModelAttribute Customer customer, ModelMap modelMap) throws CustomerNotFoundException, DuplicateCustomerException {

        service.addCustomer(customer);

        Customer theCustomer = service.getCustomers(customer.getNickname());
        modelMap.addAttribute("nickname", theCustomer.getNickname());
        modelMap.addAttribute("dateOfBirth", theCustomer.getDateOfBirth());

        return "ShowCustomer";
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.GET)
    public String goToWithdrawPage() {
        return "Withdraw";
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public String withdraw() {
        return "Withdraw";
    }

    @RequestMapping(value = "/welcome")
    public String backToWelcomePage() {
        return "Welcome";
    }

    @ExceptionHandler(Exception.class)
    public String handleException() {
        return "exception";
    }
}
