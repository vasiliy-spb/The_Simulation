package bio.world;

public class TickCounter {
    private int currentTick;
    private static final int MODULE = 1_000_000;

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
