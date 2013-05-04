package bank.icbc.domain;

import bank.icbc.common.MailSender;
import bank.icbc.database.dao.CustomerDao;
import bank.icbc.domain.enums.TransactionType;
import bank.icbc.exception.BalanceOverdrawException;
import bank.icbc.util.EmailMessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Date;

import static bank.icbc.domain.enums.TransactionType.DEPOSIT;
import static bank.icbc.domain.enums.TransactionType.WITHDRAW;

@Service("customerService")
public class CustomerService {

    @Autowired
    @Qualifier("customerDao")
    private CustomerDao customerDao;

    @Autowired
    @Qualifier("mailSender")
    private MailSender mailSender;

    public void withdraw(String nickname, double balanceToWithdraw) {
        Customer customer = customerDao.get(nickname);
        customer.setBalance(calculateBalanceLeft(nickname, balanceToWithdraw, WITHDRAW));

        customerDao.update(customer);
    }

    public void deposit(String nickname, double balanceToDeposit) {
        Customer customer = customerDao.get(nickname);
        customer.setBalance(calculateBalanceLeft(nickname, balanceToDeposit, DEPOSIT));

        Date joinDate = customer.getJoinDate();
        String dateInString = joinDate.toString();
        int joinYearInInt = getYearInInt(dateInString);

        Date now = new Date(System.currentTimeMillis());
        String todayInString = now.toString();
        int yearInInt = getYearInInt(todayInString);

        if (yearInInt - joinYearInInt > 2) {
//            ok
        } else if (yearInInt - joinYearInInt == 2 && getMonthInInt(todayInString) - getMonthInInt(dateInString) >= 0 &&
                getDateInInt(todayInString) - getDateInInt(dateInString) >= 0) {
//            ok
        }

        if (neverBeenPremiumCustomerAndNowMeetTheStandard(customer)) {
            sendEmailToManager(customer);
            customer.setPremium(true);
        }
        customerDao.update(customer);
    }

    private int getDateInInt(String todayInString) {
        String dateOfToday = todayInString.substring(8);
        return Integer.parseInt(dateOfToday);
    }

    private int getMonthInInt(String todayInString) {
        String monthOfToday = todayInString.substring(5, 7);
        return Integer.parseInt(monthOfToday);
    }

    private int getYearInInt(String todayInString) {
        String yearOfToday = todayInString.substring(0, 4);
        return Integer.parseInt(yearOfToday);
    }

    private boolean neverBeenPremiumCustomerAndNowMeetTheStandard(Customer customer) {
        int balanceAmountEnoughToBecomePremiumCustomer = 40000;
        return !customer.isPremium() &&
                customer.getBalance() > balanceAmountEnoughToBecomePremiumCustomer;
    }

    private void sendEmailToManager(Customer customer) {
        mailSender.sendEmail(EmailMessageBuilder.buildEmailMessageSendToManagerAfterCustomerBecomePremium(customer.getNickname()));
    }

    public double getBalance(String nickname) {
        Customer customer = customerDao.get(nickname);
        return customer.getBalance();
    }

    private double calculateBalanceLeft(String nickname, double balance, TransactionType transactionType) {
        if (transactionType.equals(DEPOSIT)) {
            return getBalance(nickname) + balance;
        }
        return handleWithdraw(nickname, balance);
    }

    private double handleWithdraw(String nickname, double balance) {
        double balanceAfter = getBalance(nickname) - balance;
        if (balanceAfter < 0) {
            throw new BalanceOverdrawException("You have only " + getBalance(nickname) + "$. You can not overdraw ");
        }
        return balanceAfter;
    }
}
