package bio.world;

import bio.world.entities.Creature;

import java.util.HashMap;
import java.util.Map;

public class WorldMap {
    private final Map<Creature, Coordinates> creatureMap;

    public WorldMap() {
        this.creatureMap = new HashMap<>();
    }
}
