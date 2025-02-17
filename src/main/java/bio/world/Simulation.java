package bio.world;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private final WorldMap worldMap;
    private final MoveCounter moveCounter;
    private final WorldMapRender worldMapRender;
    private final List<Actions> actionsList;

    public Simulation() {
        this.worldMap = WorldMapFactory.getRandomWorldMap();
        this.moveCounter = new MoveCounter();
        this.worldMapRender = new ConsoleMapRender();
        this.actionsList = new ArrayList<>();
    }
}
