package components;

import java.util.ArrayList;
import java.util.Arrays;

public class Cell implements Runnable {

    private int C;
    private ChannelStatus[] channelStatusList;
    private Channel[] cellChannels;
    private int numOfIdleChannels;
    private int numOfBusyChannels;
    private double[] frequencyBand;
    private boolean running;
    Call nextCall = null;
    private static CellStatus status;
    private TimeManager time = new TimeManager();

    public Cell() { // default constructor
        this.C = 0;
        this.channelStatusList = null;
        this.numOfBusyChannels = 0;
        this.numOfIdleChannels = 0;
        this.frequencyBand = null;
    }

    public Cell(int numChannels, double[] frequencyBand) {
        this.C = numChannels;
        this.numOfIdleChannels = numChannels;
        this.numOfBusyChannels = 0;
        this.frequencyBand = frequencyBand;
        this.running = true;
        this.cellChannels = new Channel[numChannels];
        initializeCellChannelList(this.cellChannels);
        status = CellStatus.CHANNELS_AVAILABLE;
        this.channelStatusList = new ChannelStatus[numChannels];
        initializeStatusList(this.channelStatusList);
    }

    private void initializeCellChannelList(Channel[] cellChannels) {
        for (int i = 0; i < cellChannels.length; i++) {
            cellChannels[i] = new Channel();
        }
    }

    public void InitializeCell(int numChannels, double[] frequencyBand) {
        this.C = numChannels;
        this.channelStatusList = new ChannelStatus[numChannels];
        initializeStatusList(this.channelStatusList);
        this.numOfIdleChannels = numChannels;
        this.numOfBusyChannels = 0;
    }

    @Override
    public void run() {
        while (running) {
            nextCall = CallHandler.getCurrentCall();
            if ( numOfIdleChannels > 0 ) {
                double interArrivalTime = nextCall.getArrivalTime();
                time.increaseTime(interArrivalTime);
                handleChannels(interArrivalTime); // handle events after increase of sim time

                assignToChannel(nextCall); // assign to channels after change in time
                numOfIdleChannels--;
                numOfBusyChannels++;
            }
            // TODO: handle time
            try {
                CallHandler.getNextCall();
            }
            catch (IndexOutOfBoundsException e) {
                System.out.println("ERROR with CallHandler.getNextCall() :: " + e);
                running = false;
            }
        }
    }

    private void initializeStatusList(ChannelStatus[] csl) {
        Arrays.fill(csl, ChannelStatus.IDLE);
    }

    public static CellStatus getStatus() {
        return status;
    }

    public Channel[] getCellChannels() {
        return this.cellChannels;
    }

    private void handleChannels(double callArrivalTime) {
        for (Channel cellChannel : this.cellChannels) {
            double totalTime = time.getSimTime();
            if (cellChannel.getStatus() == ChannelStatus.BUSY) {
                if (cellChannel.getEndOfServiceTime() <= totalTime) {
                    cellChannel.setStatus(ChannelStatus.IDLE);
                    cellChannel.setIdleTime(totalTime - cellChannel.getEndOfServiceTime());
                    numOfBusyChannels--;
                    numOfIdleChannels++;
                }
            } else {
                cellChannel.setIdleTime(cellChannel.getIdleTime() + callArrivalTime);
            }
        }
    }

    private void assignToChannel(Call call) {
        // next available channel
        // may want to change to random in future iterations
        // or assigning to server with highest time idle
        for (Channel cellChannel : this.cellChannels) {
            if (cellChannel.getStatus() == ChannelStatus.IDLE) {
                cellChannel.setStatus(ChannelStatus.BUSY);
                cellChannel.setCurrentServiceTime(call.getServiceTime());
                cellChannel.setEndOfServiceTime(time.getSimTime() + call.getServiceTime());
                // maybe keep this for tracking of total idle team
                cellChannel.setIdleTime(0);
                break;
            }
        }
    }
}
