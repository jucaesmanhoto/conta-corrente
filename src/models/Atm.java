package models;

import services.IHardware;
import services.IRemoteService;

public class Atm {

    private IHardware hardware;
    private IRemoteService remoteService;
    private Account account;

    public Atm(IHardware hardware, IRemoteService remoteService) {
        this.hardware = hardware;
        this.remoteService = remoteService;
    }

    public void logIn(String accountNumber, String pin) {
        String accountFromCard = hardware.getAccountNumberFromCard(accountNumber);
        this.account = remoteService.getAccount(accountFromCard, pin);
    }

    public void logOut(){
        account = null;
    }

    public String getBalance() {
        try {
            checkSession();
            double amount = account.getBalance() / 100.0;
            return "Your account's balance is $ " + String.format("%,.2f", amount) + ".";
        } catch (Exception e){
            return e.getMessage();
        }
    }

    public String deposit(int valueInCents) {
        try{
            checkSession();
            hardware.readEnvelope();
            account.deposit(valueInCents);
            remoteService.persistAccount();
        } catch (Exception e){
            return e.getMessage();
        }
        return "Successful Deposit.";
    }

    public String withdraw(int valueInCents) {
        try {
            checkSession();
            account.withdraw(valueInCents);
            hardware.deliverMoney();
            remoteService.persistAccount();
        } catch (Exception e){
            return e.getMessage();
        }
        return "Get your money.";
    }

    private void checkSession(){
        if (account == null)
            throw new RuntimeException("You must log in first.");
    }
}
