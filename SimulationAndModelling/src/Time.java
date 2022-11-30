import components.Cell;
import components.CellStatus;

public class Time {

    private double simTime;
    private double timeNextEvent;
    private double idleTime;

    public Time() {
        this.simTime = 0;
        this.idleTime = 0;
        this.timeNextEvent = 0;
    }

    public double getSimTime() {
        return simTime;
    }

    public double getTimeNextEvent() {
        return timeNextEvent;
    }

    public double getIdleTime() {
        return idleTime;
    }

    public void increaseTime(double timeToAdd) {
        this.simTime = this.simTime + timeToAdd;
    }

    public void setTimeNextEvent(String description, int time) {

    }
    public void increaseIdleTime(double idleTimeToAdd) {
        this.idleTime = this.idleTime + idleTimeToAdd;
    }

}
