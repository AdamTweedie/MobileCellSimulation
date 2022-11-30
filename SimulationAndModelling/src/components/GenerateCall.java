package components;

import java.util.Random;


public class GenerateCall implements Runnable {

    private Random random;
    private double averageServiceTime;
    private double arrivalRate;
    private boolean running;

    public GenerateCall(double meanServiceTime, double arrivalRate) {
        random = new Random();
        this.averageServiceTime = meanServiceTime;
        this.arrivalRate = arrivalRate;
    }


    @Override
    public void run() {

        int id = 0;
        while (running) {
            double lambda = 1 / averageServiceTime;
            double serviceTime = RandomNumberGenerator.Exponential.getRandom(random, lambda);
            double eta = 1 / arrivalRate;
            double interArrivalTime = RandomNumberGenerator.Poisson.getRandom(random, eta);

            if (serviceTime > 0) {
                log("inter-arrival time " + interArrivalTime + "    call duration " + serviceTime);
                Call call = new Call(interArrivalTime, serviceTime, id);
                CallHandler.handleCall(call);
                id++;
            } stop();
        }
    }

    public void start() {
        running = true;
        new Thread(this).start();
    }

    public void stop() {
        running = false;
    }

    private void log( String s ) {
        System.out.println( "[GenerateCall] " + s );
    }
}
