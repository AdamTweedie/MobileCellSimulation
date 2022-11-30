package components;

import java.util.ArrayList;

public class CallHandler {

    private static ArrayList<Call> incomingCalls = new ArrayList<>();
    private static Call currentCall = null;
    private static ArrayList<Call> activeCalls = new ArrayList<>();

    public static void handleCall(Call call) {
        if (currentCall == null) {
            currentCall = call;
        }
        incomingCalls.add(call);
    }

    public static ArrayList<Call> getIncomingCalls() {
        return incomingCalls;
    }

    public static Call getCurrentCall() {
        return currentCall;
    }


    public static void getNextCall() {
        incomingCalls.remove(currentCall);
        currentCall = incomingCalls.get(0);
    }
}
