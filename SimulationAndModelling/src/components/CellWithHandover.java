package components;

public abstract class CellWithHandover extends Cell {

    public CellWithHandover(int id, int numChannels, double[] frequencyBand) {
        super(id, numChannels, frequencyBand);
    }

    @Override
    public void run() {
        super.run();
    }
}
