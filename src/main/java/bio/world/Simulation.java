package bio.world;

import bio.world.actions.*;
import bio.world.entities.Herbivore;
import bio.world.factories.*;
import bio.world.render.ConsoleMapRender;
import bio.world.render.WorldMapRender;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private final WorldMap worldMap;
    private final TickCounter tickCounter;
    private final WorldMapRender worldMapRender;
    private final List<Action> initActionList;
    private final List<Action> turnActionList;

    public Simulation() {
        this.worldMap = WorldMapFactory.createWorldMapWithUserParams();
        this.tickCounter = new TickCounter();
        this.worldMapRender = new ConsoleMapRender(worldMap);
        this.initActionList = new ArrayList<>();
        this.turnActionList = new ArrayList<>();
    }

    public void start() {
        Action createFixedCountEntityAction = new CreateCustomCountEntityAction(worldMap);
        initActionList.add(createFixedCountEntityAction);
        Action makeMoveAction = new MakeMoveWithSpeedAction(worldMap, tickCounter);
        turnActionList.add(makeMoveAction);

        for (Action action : initActionList) {
            action.perform();
        }

        while (!isGameOver()) {
            nextTurn();
        }
    }

    private boolean isGameOver() {
        return worldMap.getCreatures().stream().noneMatch(c -> c instanceof Herbivore);
    }

    private void nextTurn() {
        System.out.printf("[move: %d]\n", tickCounter.getCurrentTick());
        for (Action action : turnActionList) {
            action.perform();
            worldMapRender.renderMap();
            System.out.println();
        }
        tickCounter.next();
    }
}
