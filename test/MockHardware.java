import services.IHardware;

import java.util.ArrayList;
import java.util.List;

public class MockHardware implements IHardware {
    List<String> methodCalls = new ArrayList<>();

    @Override
    public String getAccountNumberFromCard(String numberFromCard) {
        methodCalls.add("getAccountNumberFromCard");

        if (numberFromCard == "fail"){
            throwsRuntimeException();
        }
        return numberFromCard;
    }

    @Override
    public void deliverMoney() {
        methodCalls.add("deliverMoney");
    }

    @Override
    public void readEnvelope() {
        methodCalls.add("readEnvelope");
    }

    public void throwsRuntimeException(){
        throw new RuntimeException("Sorry. ATM error.");
    }

    public List<String> verifyMethods(){
        return methodCalls;
    }
}
