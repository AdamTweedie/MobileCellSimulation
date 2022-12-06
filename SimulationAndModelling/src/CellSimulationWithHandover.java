import components.CellWithHandover;
import components.Channel;
import components.GenerateCall;
import components.GenerateCallWithHandovers;

import java.text.DecimalFormat;

public class CellSimulationWithHandover {

    public static void main(String[] args) {
        int numCalls = 10000;
        double meanServiceTime = 100;
        double arrivalRate = 0.1;
        double handoverRate = 0.03;

        for (double i = 0.01; i < 0.1; i += 0.01) {
            GenerateCallWithHandovers gch = new GenerateCallWithHandovers(meanServiceTime, i,
                    numCalls, handoverRate, meanServiceTime);
            gch.run();
            CellWithHandover cellWithHandover = new CellWithHandover(0, 16, new double[]{0.05, 0.08}, 2);
            cellWithHandover.run();

            DecimalFormat df = new DecimalFormat("#.##");
            double CBP = (double) cellWithHandover.getTotalNewCallLoss() / cellWithHandover.getTotalNewCallArrival();
            double HFP = (double) cellWithHandover.getHandoverLoss() / cellWithHandover.getHandoverGain();
            double ABP = (double) CBP + (10*HFP);

            System.out.println("[CALL RATE: " + df.format(i) + ", ABP = " + df.format(ABP));
        }
    }
}
