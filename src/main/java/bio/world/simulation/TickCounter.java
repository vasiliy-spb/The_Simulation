package bio.world.simulation;

public class TickCounter {
    private static final int MODULE = 1_000_000;
    private int currentTick;

    public TickCounter() {
        this.currentTick = 0;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public int next() {
        currentTick %= MODULE;
        return ++currentTick;
    }
}
