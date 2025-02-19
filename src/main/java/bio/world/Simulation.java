package bio.world;

import bio.world.actions.Action;
import bio.world.actions.CreateFixedCountEntityAction;
import bio.world.actions.MakeMoveAction;
import bio.world.actions.MakeMoveWithSpeedAction;
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
        this.worldMap = WorldMapFactory.getRandomWorldMap();
        this.tickCounter = new TickCounter();
        this.worldMapRender = new ConsoleMapRender(worldMap);
        this.initActionList = new ArrayList<>();
        this.turnActionList = new ArrayList<>();
    }

    public void startWithoutSpeed() {
        int height = worldMap.getHeight();
        int width = worldMap.getWidth();
        int maxEntityNumber = height * width;
        int countEntity = maxEntityNumber / 6;
        Action createFixedCountEntityAction = new CreateFixedCountEntityAction(worldMap, countEntity);
        initActionList.add(createFixedCountEntityAction);
        Action makeMoveAction = new MakeMoveAction(worldMap);
        turnActionList.add(makeMoveAction);

        for (Action action : initActionList) {
            action.perform();
            worldMapRender.renderMap();
            System.out.println();
        }

        int moveCount = 6;
        while (moveCount-- > 0) {
            for (Action action : turnActionList) {
                action.perform();
                worldMapRender.renderMap();
                System.out.println();
            }
        }
    }

    public void start() {
        int height = worldMap.getHeight();
        int width = worldMap.getWidth();
        int maxEntityNumber = height * width;
        int countEntity = maxEntityNumber / 6;
        Action createFixedCountEntityAction = new CreateFixedCountEntityAction(worldMap, countEntity);
        initActionList.add(createFixedCountEntityAction);
        Action makeMoveAction = new MakeMoveWithSpeedAction(worldMap, tickCounter);
        turnActionList.add(makeMoveAction);

        for (Action action : initActionList) {
            action.perform();
            worldMapRender.renderMap();
            System.out.println();
        }

        int moveCount = 10;
        while (tickCounter.getCurrentTick() < moveCount) {
            System.out.printf("[move: %d]\n", tickCounter.getCurrentTick());
            for (Action action : turnActionList) {
                action.perform();
                worldMapRender.renderMap();
                System.out.println();
            }
            tickCounter.next();
        }
    }
}
