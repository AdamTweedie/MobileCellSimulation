package components;

import java.util.ArrayList;
import java.util.Arrays;

public class Cell implements Runnable {

    private Channel[] cellChannels;
    private int numOfIdleChannels;
    private double[] frequencyBand;
    private boolean running;
    private static CellStatus status;
    private int cellId;
    private TimeManager time = new TimeManager();

    public Cell(int id, int numChannels, double[] frequencyBand) {
        this.cellId = id;
        this.numOfIdleChannels = numChannels;
        this.frequencyBand = frequencyBand;
        this.running = true;
        this.cellChannels = new Channel[numChannels];
        initializeCellChannelList(this.cellChannels);
        status = CellStatus.CHANNELS_AVAILABLE;
    }

    private void initializeCellChannelList(Channel[] cellChannels) {
        for (int i = 0; i < cellChannels.length; i++) {
            cellChannels[i] = new Channel(i);
        }
    }

    @Override
    public void run() {
        while (running) {
            Call nextCall = CallHandler.getCurrentCall();
            if ( numOfIdleChannels > 0 ) {
                double interArrivalTime = nextCall.getArrivalTime();
                time.increaseTime(interArrivalTime);
                handleChannels(interArrivalTime); // handle events after increase of sim time

                assignToChannel(nextCall); // assign to channels after change in time
            }

            try {
                CallHandler.getNextCall();
            }
            catch (IndexOutOfBoundsException e) {
                System.out.println("ERROR with CallHandler.getNextCall() :: " + e);
                running = false;
            }
        }
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
                    Events.log(this.cellId, cellChannel.getId(), cellChannel.getCallId(),
                            cellChannel.getEndOfServiceTime(), Events.EventType.DEPARTURE);
                    cellChannel.setStatus(ChannelStatus.IDLE);
                    cellChannel.setIdleTime(totalTime - cellChannel.getEndOfServiceTime());
                    time.setTimeNextEvent(Events.EventType.DEPARTURE.toString(), (int) cellChannel.getEndOfServiceTime());
                    numOfIdleChannels++;
                }
            } else {
                double newIdleTime = cellChannel.getIdleTime() + callArrivalTime;
                cellChannel.setIdleTime(newIdleTime);
            }
        }
    }

    private void assignToChannel(Call call) {
        // may want to change to random in future iterations
        boolean foundChannel = false;
        for (Channel cellChannel : this.cellChannels) {
            if (cellChannel.getStatus() == ChannelStatus.IDLE) {
                foundChannel = true;
                Events.log(this.cellId, cellChannel.getId(), call.getNumber(),
                        time.getSimTime(), Events.EventType.ARRIVAL);
                cellChannel.setStatus(ChannelStatus.BUSY);
                cellChannel.setCurrentServiceTime(call.getServiceTime());
                cellChannel.setEndOfServiceTime(time.getSimTime() + call.getServiceTime());
                cellChannel.setCallId(call.getNumber());
                // maybe keep this for tracking of total idle team
                cellChannel.setIdleTime(0);
                numOfIdleChannels++;
                break;
            }
        }
        if (!foundChannel) {
            Events.log(this.cellId, null, null, time.getSimTime(),
                    Events.EventType.CELL_AT_CAPACITY);
            time.setTimeNextEvent(Events.EventType.CELL_AT_CAPACITY.toString(), (int) time.getSimTime());
        }

    }

    private static void log(String logText) {
        System.out.println("[[Cell] " + logText+"]");
    }
}
