package components;

import java.io.Serializable;

public class Call implements Serializable {

    private double interArrivalTime; // time at which the customer arrived
    private double serviceTime; // time spent in the system
    private int number;
    public Call() {  // default constructor
        this.interArrivalTime = 0;
        this.serviceTime = 0;
    }

    public Call(double arrTime, double callDuration, int number) {
        this.interArrivalTime = arrTime;
        this.serviceTime = callDuration;
        this.number = number;
    }

    public double getArrivalTime() {
        return interArrivalTime;
    }

    public double getServiceTime() {
        return serviceTime;
    }

    public int getNumber() {
        return this.number;
    }
}
