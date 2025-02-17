package bio.world;

import bio.world.actions.Action;
import bio.world.actions.CreateFixedCountEntityAction;
import bio.world.factories.*;
import bio.world.render.ConsoleMapRender;
import bio.world.render.WorldMapRender;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private final WorldMap worldMap;
    private final MoveCounter moveCounter;
    private final WorldMapRender worldMapRender;
    private final List<Action> actionList;

    public Simulation() {
        this.worldMap = WorldMapFactory.getRandomWorldMap();
        this.moveCounter = new MoveCounter();
        this.worldMapRender = new ConsoleMapRender(worldMap);
        this.actionList = new ArrayList<>();
    }

    public void start() {
        int height = worldMap.getHeight();
        int width = worldMap.getWidth();
        int maxEntityNumber = height * width;
        int countEntity = maxEntityNumber / 6;
        Action createFixedCountEntityAction = new CreateFixedCountEntityAction(worldMap, countEntity);
        actionList.add(createFixedCountEntityAction);

        for (Action action : actionList) {
            action.perform();
        }
        worldMapRender.renderMap();
    }
}
