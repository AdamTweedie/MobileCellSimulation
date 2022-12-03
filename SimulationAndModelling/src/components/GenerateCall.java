package components;

import java.util.Random;


public class GenerateCall implements Runnable {

    private Random random;
    private double averageServiceTime;
    private double arrivalRate;
    private int numCalls;

    public GenerateCall(double meanServiceTime, double arrivalRate, int numberOfCalls) {
        random = new Random();
        this.averageServiceTime = meanServiceTime;
        this.arrivalRate = arrivalRate;
        this.numCalls = numberOfCalls;
    }

    @Override
    public void run() {
        try {
            int id = 0;
            for (int i = numCalls; i>0; i--) {
                double lambda = 1 / averageServiceTime;
                double serviceTime = RandomNumberGenerator.Exponential.getRandom(random, lambda);
                double eta = 1 / arrivalRate;
                double interArrivalTime = RandomNumberGenerator.Poisson.getRandom(random, eta);
                if (serviceTime > 0) {
                    log("id:"+id+" inter-arrival time " + interArrivalTime + "    call duration " + serviceTime);
                    Call call = new Call(interArrivalTime, serviceTime, id);
                    CallHandler.handleCall(call);
                    id++;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void log( String s ) {
        System.out.println( "[GenerateCall] " + s );
    }
}
