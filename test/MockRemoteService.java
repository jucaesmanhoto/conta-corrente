import models.Account;
import services.IRemoteService;

import java.util.ArrayList;
import java.util.List;

public class MockRemoteService implements IRemoteService {

    private String mockedAccountNumber;
    private String mockedAccountPin;
    private int mockedAccountBalance = 0;
    List<String> methodCalls = new ArrayList<>();

    public void setMockedAccountNumber(String mockedAccountNumber) {
        this.mockedAccountNumber = mockedAccountNumber;
    }

    public void setMockedAccountPin(String mockedAccountPin) {
        this.mockedAccountPin = mockedAccountPin;
    }

    public void setMockedAccountBalance(int mockedAccountBalance) {
        this.mockedAccountBalance = mockedAccountBalance;
    }

    @Override
    public Account getAccount(String accountNumber, String pin) {
        methodCalls.add("getAccount");
        if(accountNumber != mockedAccountNumber || pin != mockedAccountPin)
            throw new RuntimeException("Unable to access this account.");

        Account account = new Account();
        account.setBalance(mockedAccountBalance);
        account.setAccountNumber(mockedAccountNumber);
        return account;
    }

    @Override
    public Account persistAccount() {
        methodCalls.add("persistAccount");
        Account account = new Account();
        account.setBalance(mockedAccountBalance);
        account.setAccountNumber(mockedAccountNumber);
        return account;
    }

    public List<String> verifyMethods() {
        return methodCalls;
    }
}

