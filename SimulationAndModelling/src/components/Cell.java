package components;

import java.util.ArrayList;
import java.util.Arrays;

public class Cell implements Runnable {

    private int C;
    private Channel[] cellChannels;
    private double[] frequencyBand;
    private boolean running;
    private CellStatus status;
    private int cellId;
    private int totalNewCallArrival;
    private int totalNewCallLoss;
    private double serverUtilization;
    private TimeManager time = new TimeManager();

    public Cell(int id, int numChannels, double[] frequencyBand) {
        this.cellId = id;
        this.C = numChannels;
        this.status = CellStatus.CHANNELS_AVAILABLE;
        this.frequencyBand = frequencyBand;
        this.totalNewCallArrival = 0;
        this.totalNewCallLoss = 0;
        this.serverUtilization = 0;
        this.running = true;
        this.cellChannels = new Channel[numChannels];
        initializeCellChannelList(this.cellChannels);

    }

    private void initializeCellChannelList(Channel[] cellChannels) {
        for (int i = 0; i < cellChannels.length; i++) {
            cellChannels[i] = new Channel(i);
        }
    }

    @Override
    public void run() {
        while (running) {
            Call nextCall = CallHandler.getCurrentCall(); // 100, 56
            double interArrivalTime = nextCall.getArrivalTime();
            time.increaseTime(interArrivalTime); // 100
            handleChannels(interArrivalTime);
            if (numFreeChannels() > 0) {
                assignToChannel(nextCall);
                this.totalNewCallArrival++;
            } else {
                this.totalNewCallLoss++;
            }
            this.serverUtilization += setServerUtilization();
            try {
                CallHandler.getNextCall();
            } catch (IndexOutOfBoundsException e) {
                //System.out.println("ERROR with CallHandler.getNextCall() :: " + e);
                running = false;
            }
        }
    }

    public CellStatus getStatus() {
        return this.status;
    }

    public Channel[] getCellChannels() {
        return this.cellChannels;
    }

    public int numFreeChannels() {
        int count = 0;
        for (Channel c : this.cellChannels) {
            if (c.getStatus().equals(ChannelStatus.IDLE)) count++;
        }
        return count;
    }

    public void handleChannels(double callArrivalTime) {
        for (Channel cellChannel : this.cellChannels) {
            double totalTime = time.getSimTime();
            if (cellChannel.getStatus() == ChannelStatus.BUSY) {
                if (cellChannel.getEndOfServiceTime() <= totalTime) {
                    //Events.log(this.cellId, cellChannel.getId(), cellChannel.getCallId(),
                     //       cellChannel.getEndOfServiceTime(), Events.EventType.DEPARTURE);
                    cellChannel.setStatus(ChannelStatus.IDLE);
                    cellChannel.setIdleTime(totalTime - cellChannel.getEndOfServiceTime());
                    time.setTimeNextEvent(Events.EventType.DEPARTURE.toString(), (int) cellChannel.getEndOfServiceTime());
                    status = CellStatus.CHANNELS_AVAILABLE;
                }
            } else {
                double newIdleTime = cellChannel.getIdleTime() + callArrivalTime;
                cellChannel.setIdleTime(newIdleTime);
                status = CellStatus.CHANNELS_AVAILABLE;
            }
        }
    }

    public void assignToChannel(Call call) {
        // may want to change to random in future iterations
        boolean foundChannel = false;
        for (Channel cellChannel : this.cellChannels) {
            if (cellChannel.getStatus() == ChannelStatus.IDLE) {
                foundChannel = true;
                //Events.log(this.cellId, cellChannel.getId(), call.getNumber(),
                //        time.getSimTime(), Events.EventType.ARRIVAL);
                cellChannel.setStatus(ChannelStatus.BUSY);
                cellChannel.setCurrentServiceTime(call.getServiceTime());
                cellChannel.setEndOfServiceTime(time.getSimTime() + call.getServiceTime());
                cellChannel.setCallId(call.getNumber());
                time.setTimeNextEvent(Events.EventType.ARRIVAL.toString(), (int) time.getSimTime());
                // maybe keep this for tracking of total idle team
                cellChannel.setIdleTime(0);
                break;
            }
        }
        if (!foundChannel) {
            System.out.println("SHOULDNT HAVE REACHED HERE");
            //Events.log(this.cellId, null, null, time.getSimTime(),
            //        Events.EventType.CELL_AT_CAPACITY);
            time.setTimeNextEvent(Events.EventType.CELL_AT_CAPACITY.toString(), (int) time.getSimTime());
            this.totalNewCallLoss++;
            this.status = CellStatus.NO_CHANNELS_AVAILABLE;
        }
    }

    private double setServerUtilization() {
        int count = 0;
        for (Channel channel : this.getCellChannels()) {
            if (channel.getStatus() == ChannelStatus.BUSY) {
                count++;
            }
        }
        return ((double) count / this.C);
    }

    public boolean getRunning() {
        return this.running;
    }

    public TimeManager getTime() {
        return this.time;
    }

    public void incrementTotalNewCallLoss() {
        this.totalNewCallLoss++;
    }

    public void incrementTotalNewCallAccepted() {
        this.totalNewCallArrival++;
    }

    public double getServerUtilization() {
        return this.serverUtilization;
    }

    public int getTotalNewCallArrival() {
        return this.totalNewCallArrival;
    }

    public int getTotalNewCallLoss() {
        return this.totalNewCallLoss;
    }
}
