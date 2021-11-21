import static org.junit.Assert.*;

import models.Atm;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AtmTest {
    private MockRemoteService mockRemoteService;
    private MockHardware mockHardware;
    private Atm sut;

    @Before
    public void setUp() {
        mockRemoteService = new MockRemoteService();
        mockHardware = new MockHardware();
        sut = new Atm(mockHardware, mockRemoteService);
        setMockAccountValues(null, null);
    }

    public void setMockAccountValues(String accountNumber, Integer accountBalance) {
        if (accountBalance == null)
            accountBalance = 0;
        mockRemoteService.setMockedAccountBalance(accountBalance);
        if (accountNumber == null)
            accountNumber = "3.164.256-4";
        mockRemoteService.setMockedAccountNumber(accountNumber);
        mockRemoteService.setMockedAccountPin("1234");
        sut.logIn(accountNumber, "1234");
    }

    @Test
    public void test01_getInitialAccountBalance() {

        String actual = sut.getBalance();
        String expected = "Your account's balance is $ 0,00.";
        assertEquals(expected, actual);
        assertTrue(mockHardware.verifyMethods().contains("getAccountNumberFromCard"));
    }

    @Test
    public void test02_makeASuccessfulDeposit() {
        setMockAccountValues(null, 350);

        String actual = sut.deposit(350);
        String expected = "Successful Deposit.";
        assertEquals(expected, actual);
        assertTrue(mockHardware.verifyMethods().contains("readEnvelope"));
        assertTrue(mockRemoteService.verifyMethods().contains("persistAccount"));
    }

    @Test
    public void test03_makeASuccessfulWithdraw() {
        setMockAccountValues(null, 350);

        String actual = sut.withdraw(350);
        String expected = "Get your money.";
        assertEquals(expected, actual);
        assertTrue(mockHardware.verifyMethods().contains("deliverMoney"));
        assertTrue(mockRemoteService.verifyMethods().contains("persistAccount"));
    }

    @Test
    public void test04_getTheRightBalanceAfterDeposit() {
        setMockAccountValues(null, 350);

        String actual = sut.deposit(123);
        String expected = "Successful Deposit.";
        assertEquals(expected, actual);
        actual = sut.getBalance();
        expected = "Your account's balance is $ 4,73.";
        assertEquals(expected, actual);
        assertTrue(mockHardware.verifyMethods().contains("readEnvelope"));
        assertTrue(mockRemoteService.verifyMethods().contains("persistAccount"));
    }

    @Test
    public void test05_getTheRightBalanceAfterWithdraw() {
        setMockAccountValues(null, 350);

        String actual = sut.withdraw(172);
        String expected = "Get your money.";
        assertEquals(expected, actual);
        actual = sut.getBalance();
        expected = "Your account's balance is $ 1,78.";
        assertEquals(expected, actual);
        assertTrue(mockHardware.verifyMethods().contains("deliverMoney"));
        assertTrue(mockRemoteService.verifyMethods().contains("persistAccount"));
    }

    @Test
    public void test06_throwErrorForWithdraw() {
        setMockAccountValues(null, 350);
        String actual = sut.withdraw(762);
        String expected = "Insufficient balance.";

        assertEquals(expected, actual);
        assertFalse(mockHardware.verifyMethods().contains("deliverMoney"));
        assertFalse(mockRemoteService.verifyMethods().contains("persistAccount"));
    }

    @Test
    public void test07_ReturnErrorMessageForLogOutActions() {
        sut.logOut();
        String actual = sut.withdraw(762);
        String expected = "You must log in first.";
        assertEquals(expected, actual);

        actual = sut.getBalance();
        assertEquals(expected, actual);

        actual = sut.deposit(765);
        assertEquals(expected, actual);
    }

    @Test
    public void test08_unsuccessfulLogin() {
        sut.logOut();
        String actual = "";
        try {
            sut.logIn("3.164.256-4", "546778");
            fail();
        } catch (Exception e) {
            actual = e.getMessage();
        }

        String expected = "Unable to access this account.";
        assertEquals(expected, actual);
    }

    @Test
    public void test09_atmFailsToGetCardNumber() {
        String actual = "";
        try {
            sut.logIn("fail", "546778");
            fail();
        } catch (Exception e) {
            actual = e.getMessage();
        }

        String expected = "Sorry. ATM error.";
        assertEquals(expected, actual);
        assertFalse(mockRemoteService.verifyMethods().contains("persistAccount"));
    }
}