package components;

public class GenerateCallWithHandovers extends GenerateCall {
    private double handoverRate;
    private double serviceTime;
    public GenerateCallWithHandovers(double meanServiceTime, double arrivalRate, int numberOfCalls) {
        super(meanServiceTime, arrivalRate, numberOfCalls);
    }

    public void initializeHandoverRates( double handoverRate, int meanHandoverServiceTime) {
        this.handoverRate = handoverRate;
        this.serviceTime = meanHandoverServiceTime;
    }

    @Override
    public void run() {
        try {
            int id = 0;
            for (int i = this.getNumCalls(); i>0; i--) {
                double lambdaArrival = 1 / this.handoverRate;
                double lambdaService = 1 / this.serviceTime;
                int serviceTime = (int) RandomNumberGenerator.Exponential.getRandom(this.getRandom(), lambdaService);
                int handoverArrivalTime = (int) RandomNumberGenerator.Poisson.getRandom(this.getRandom(), lambdaArrival);
                if (serviceTime > 0) {
                    Call call = new Call(handoverArrivalTime, serviceTime, id);
                    CallHandler.handleHandoverCall(call);
                    id++;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        super.run();
    }
}
