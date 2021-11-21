package services;

import models.Account;

public interface IRemoteService {

    public Account getAccount(String accountNumber, String pin);
    public Account persistAccount();
}
