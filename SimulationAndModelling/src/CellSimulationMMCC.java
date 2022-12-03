import components.*;

public class CellSimulationMMCC {
    /***
     * event:
     *  - Arrival
     *  - Departure
     *  - End-Simulation
     *
     * Variables:
     *  - sim_time, time_last_event
     *  - next_event_type
     *  - time_next_event
     *  - total_loss
     *  - area_service_status
     */

    public static void main(String[] args) {
        GenerateCall gc = new GenerateCall(100, 0.01, 100);
        gc.run();

        Cell cell = new Cell(16, new double[]{0.001, 0.005});
        cell.run();



        for (Channel channel : cell.getCellChannels()) {
            System.out.println(channel.getStatus());
        }
    }
}
