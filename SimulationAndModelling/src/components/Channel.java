package components;

public class Channel {

    private ChannelStatus status;
    private double serviceTime;
    private double idleTime;
    private double endOfServiceTime;

    public Channel() {
        this.status = ChannelStatus.IDLE;
        this.serviceTime = 0;
        this.idleTime = 0;
        this.endOfServiceTime = 0;
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

    public double getServiceTime() {
        return this.serviceTime;
    }

    public void setEndOfServiceTime(double endOfServiceTime) {
        this.endOfServiceTime = endOfServiceTime;
    }

    public double getEndOfServiceTime() {
        return endOfServiceTime;
    }
}
