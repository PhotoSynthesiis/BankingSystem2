package bank.icbc.controller;

import bank.icbc.domain.Bank;
import bank.icbc.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CustomerController {

    @Autowired
    @Qualifier("bank")
    private Bank bank;

    @Autowired
    @Qualifier("customer")
    private Customer customer;

    @RequestMapping(value = "/addCustomer", method = RequestMethod.GET)
    public String addCustomer(@ModelAttribute Customer customer) {
        return "AddCustomer";
    }

    @RequestMapping(value = "/addCustomer", method = RequestMethod.POST)
    public String saveCustomer(@ModelAttribute Customer customer, ModelMap modelMap) {
        bank.addCustomer(customer);
        Customer theCustomer = bank.getCustomer(customer.getNickname());

        modelMap.addAttribute("nickname", theCustomer.getNickname());
        modelMap.addAttribute("dateOfBirth", theCustomer.getDateOfBirth());
        modelMap.addAttribute("balance", theCustomer.getBalance());
        modelMap.addAttribute("emailAddress", theCustomer.getEmailAddress());

        return "ShowCustomer";
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.GET)
    public String goToWithdrawPage(@ModelAttribute Customer customer) {
        return "Withdraw";
    }

    @RequestMapping(value = "/deposit", method = RequestMethod.GET)
    public String goToDepositPage(@ModelAttribute Customer customer) {
        return "Deposit";
    }

    @RequestMapping(value = "/deposit", method = RequestMethod.POST)
    public String deposit(@ModelAttribute("customer") Customer customerObj, ModelMap modelMap) {
        customer.deposit(customerObj.getBalance(), customerObj.getNickname());

        Customer theCustomer = bank.getCustomer(customerObj.getNickname());
        modelMap.addAttribute("nickname", theCustomer.getNickname());
        modelMap.addAttribute("balance", theCustomer.getBalance());

        return "ShowBalance";
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public String withdraw(@ModelAttribute("customer") Customer customerObj, ModelMap modelMap) {
        customer.withdraw(customerObj.getBalance(), customerObj.getNickname());

        Customer theCustomer = bank.getCustomer(customerObj.getNickname());

        modelMap.addAttribute("nickname", theCustomer.getNickname());
        modelMap.addAttribute("balance", theCustomer.getBalance());

        return "ShowBalance";
    }

    @RequestMapping(value = "/welcome")
    public String backToWelcomePage() {
        return "Welcome";
    }

    @ExceptionHandler(Exception.class)
    public String handleException() {
        return "Exception";
    }
}
