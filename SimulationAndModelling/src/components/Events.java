package components;

public class Events {

    // arrival
    // departure
    public static void log(int cellId, Object channelId, Object callId,
                           double eventTime, EventType eventType) {
        System.out.println("[CELL EVENT: " + eventType +
                "] Time:" + eventTime + "s" +
                " CellId:" + cellId +
                " ChannelId:" + channelId +
                ", CallId:" + callId);
    }

    public enum EventType {
        ARRIVAL,
        DEPARTURE,
        CELL_AT_CAPACITY,
        CELL_IDLE
    }
}


