package bio.world;

import java.util.Random;

public abstract class EntityFactory<T> {
    protected final Random random = new Random();
    public abstract T createInstance(WorldMap worldMap);
}
