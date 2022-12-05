package components;

import java.util.ArrayList;

public class CallHandler {

    private static ArrayList<Call> incomingCalls = new ArrayList<>();
    private static ArrayList<Call> incomingHandoverCalls = new ArrayList<>();
    private static Call currentCall = null;
    private static Call currentHandoverCall = null;
    private static ArrayList<Call> activeCalls = new ArrayList<>();

    public static void handleCall(Call call) {
        if (currentCall == null) {
            currentCall = call;
        }
        incomingCalls.add(call);
    }

    public static void handleHandoverCall(Call call) {
        if (currentHandoverCall == null) {
            currentHandoverCall = call;
        }
        incomingHandoverCalls.add(call);
    }

    public static ArrayList<Call> getIncomingCalls() {
        return incomingCalls;
    }

    public static Call getCurrentCall() {
        return currentCall;
    }

    public static Call getCurrentHandoverCall() {
        return currentHandoverCall;
    }

    public static void getNextCall() {
        incomingCalls.remove(currentCall);
        currentCall = incomingCalls.get(0);
    }

    public static int getAmountOfHandoverCalls() {
        return incomingHandoverCalls.size();
    }

    public static ArrayList<Call> getIncomingHandoverCalls() {
        return incomingHandoverCalls;
    }

    public static void getNextHandoverCall() {
        incomingHandoverCalls.remove(currentHandoverCall);
        currentHandoverCall = incomingHandoverCalls.get(0);
    }
}
