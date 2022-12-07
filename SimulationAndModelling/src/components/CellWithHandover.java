package components;

public class CellWithHandover extends Cell {

    Call incomingHandoverCall;
    Call incomingCall;
    int handoverLoss;
    int handoverGain;
    int n1;
    boolean running;

    public CellWithHandover(int id, int numChannels, double freq, int threshold) {
        super(id, numChannels, freq);
        this.n1 = threshold;
        this.handoverLoss = 0;
        this.handoverGain = 0;
        this.running = true;
    }

    @Override
    public void run() {
        while (running) {
            setIncomingCall(CallHandler.getCurrentCall());
            setHandoverCall(CallHandler.getCurrentHandoverCall());
            double incomingHandoverTime = this.incomingHandoverCall.getArrivalTime();
            double incomingCallTime = this.incomingCall.getArrivalTime();
            if (incomingCallTime > incomingHandoverTime) {
                getTime().increaseTime(incomingHandoverTime);
                this.incomingCall.setInterArrivalTime(incomingCallTime - incomingHandoverTime);
                handleChannels(incomingHandoverTime);
                if (numFreeChannels() > 0) {
                    this.incomingHandoverCall.setFreq(getFreq());
                    assignToChannel(this.incomingHandoverCall);
                    this.handoverGain++;
                } else {
                    this.handoverLoss++;
                }
                try {
                    CallHandler.getNextHandoverCall();
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("IndexOutOfBoundsException for IncomingHandoverCalls ArrayList");
                    running = false;
                }
            } else if (incomingHandoverTime > incomingCallTime) {
                getTime().increaseTime(incomingCallTime);
                this.incomingHandoverCall.setInterArrivalTime(incomingHandoverTime - incomingCallTime);
                handleChannels(incomingCallTime);
                if (numFreeChannels() > n1) {
                    assignToChannel(this.incomingCall);
                    this.incomingCall.setFreq(getFreq());
                    incrementTotalNewCallAccepted();
                } else {
                    incrementTotalNewCallLoss();
                }
                try {
                    CallHandler.getNextCall();
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("IndexOutOfBoundsException for IncomingCalls ArrayList");
                    running = false;
                }
            } else {
                assert incomingCallTime == incomingHandoverTime;
                getTime().increaseTime(incomingCallTime);
                handleChannels(incomingCallTime);
                if (numFreeChannels() > 0) {
                    assignToChannel(this.incomingHandoverCall);
                    this.incomingHandoverCall.setFreq(getFreq());
                    this.handoverGain++;
                } else {
                    this.handoverLoss++;
                }
                if (numFreeChannels() > n1) {
                    assignToChannel(this.incomingCall);
                    this.incomingCall.setFreq(getFreq());
                    incrementTotalNewCallAccepted();
                } else {
                    incrementTotalNewCallLoss();
                }
                try {
                    CallHandler.getNextCall();
                    CallHandler.getNextHandoverCall();
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("IndexOutOfBoundsException at Handover list or Incoming list");
                    running = false;
                }
            }
        }
    }

    private void setHandoverCall(Call handoverCall) {
        this.incomingHandoverCall = handoverCall;
    }

    private void setIncomingCall(Call currentCall) {
        this.incomingCall = currentCall;
    }

    public int getHandoverGain() {
        return handoverGain;
    }

    public int getHandoverLoss() {
        return handoverLoss;
    }
}
