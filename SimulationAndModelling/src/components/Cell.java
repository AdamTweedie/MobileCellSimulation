package components;

import java.sql.Time;
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
        Time time = new Time(0);
        while (running) {
            nextCall = CallHandler.getCurrentCall();
            if ( numOfIdleChannels > 0 ) {
                System.out.println(nextCall);
                assignToChannel(nextCall);
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
        return cellChannels;
    }

    private static void assignToChannel(Call call) {

    }
}
