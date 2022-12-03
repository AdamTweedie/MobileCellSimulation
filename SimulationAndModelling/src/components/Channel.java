package components;

public class Channel {

    private ChannelStatus status;
    private double serviceTime;
    private double idleTime;
    private double endOfServiceTime;
    private int id;
    private int callId;

    public Channel(int channelId) {
        this.id = channelId;
        this.status = ChannelStatus.IDLE;
        this.serviceTime = 0;
        this.idleTime = 0;
        this.endOfServiceTime = 0;
        this.callId = 0;
    }

    public void setStatus(ChannelStatus status) {
        this.status = status;
    }

    public void increaseIdleTime(double timeIdle) {
        this.idleTime = this.idleTime + timeIdle;
    }

    public double getIdleTime() {
        return this.idleTime;
    }

    public ChannelStatus getStatus() {
        return this.status;
    }

    public void setCurrentServiceTime(double serviceDuration) {
        this.serviceTime = serviceDuration;
    }

    public void setIdleTime(double idleTime) {
        this.idleTime = idleTime;
    }

    public int getId() {
        return id;
    }

    public double getServiceTime() {
        return this.serviceTime;
    }

    public void setEndOfServiceTime(double endOfServiceTime) {
        this.endOfServiceTime = endOfServiceTime;
    }

    public double getEndOfServiceTime() {
        return endOfServiceTime;
    }

    public void setCallId(int callId) {
        this.callId = callId;
    }

    public int getCallId() {
        return callId;
    }
}
