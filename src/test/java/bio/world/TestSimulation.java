package bio.world;

import bio.world.actions.*;
import bio.world.auxiliary_actions.*;
import bio.world.entities.Coordinates;
import bio.world.entities.Entity;
import bio.world.map.WorldMap;
import bio.world.render.ConsoleMapRender;
import bio.world.render.WorldMapRender;
import bio.world.factories.WorldMapFactoryForTests;
import bio.world.simulation.TickCounter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TestSimulation {
    private final WorldMap worldMap;
    private final TickCounter tickCounter;
    private final WorldMapRender worldMapRender;
    private final List<Action> initActionList;
    private final List<Action> turnActionList;

    public TestSimulation(String templateFilePath) {
        this.worldMap = WorldMapFactoryForTests.createWorldMapByTemplate(templateFilePath);
        this.tickCounter = new TickCounter();
        this.worldMapRender = new ConsoleMapRender(worldMap);
        this.initActionList = new ArrayList<>();
        this.turnActionList = new ArrayList<>();
    }

    public TestSimulation(int height, int width) {
        this.worldMap = WorldMapFactoryForTests.getWorldMap(height, width);
        this.tickCounter = new TickCounter();
        this.worldMapRender = new ConsoleMapRender(worldMap);
        this.initActionList = new ArrayList<>();
        this.turnActionList = new ArrayList<>();
    }

    public void startWithoutSpeed() {
        int height = worldMap.getHeight();
        int width = worldMap.getWidth();
        int maxEntityNumber = height * width;
        int countEntity = 0;
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
        int countEntity = 0;
        Action createFixedCountEntityAction = new CreateFixedCountEntityAction(worldMap, countEntity);
        initActionList.add(createFixedCountEntityAction);
        Action makeMoveAction = new MakeMoveWithSpeedAction(worldMap, tickCounter);
        turnActionList.add(makeMoveAction);

        for (Action action : initActionList) {
            action.perform();
            worldMapRender.renderMap();
            System.out.println();
        }

        int moveCount = 20;
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

    public void start(int countEntity) {
        Action createFixedCountEntityAction = new CreateFixedCountEntityAction(worldMap, countEntity);
        initActionList.add(createFixedCountEntityAction);
        Action makeMoveAction = new MakeMoveWithSpeedAction(worldMap, tickCounter);
        turnActionList.add(makeMoveAction);

        for (Action action : initActionList) {
            action.perform();
            worldMapRender.renderMap();
        }

        System.out.println();

        int moveCount = 20;
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

    public void startPredatorsOnlyWithoutSpeed() {
        startPredatorsOnlyWithoutSpeed(1);
    }

    public void startPredatorsOnlyWithoutSpeed(int moveCount) {
        int countEntity = 0;
        Action createFixedCountEntityAction = new CreateFixedCountEntityAction(worldMap, countEntity);
        initActionList.add(createFixedCountEntityAction);
        Action makeMoveAction = new OnlyPredatorsMakeMoveAction(worldMap);
        turnActionList.add(makeMoveAction);

        for (Action action : initActionList) {
            action.perform();
            worldMapRender.renderMap();
            System.out.println();
        }

        while (moveCount-- > 0) {
            for (Action action : turnActionList) {
                action.perform();
                worldMapRender.renderMap();
                System.out.println();
            }
        }
    }

    public void startPredatorsOnly() {
        startPredatorsOnly(1);
    }

    public void startPredatorsOnly(int moveCount) {
        int countEntity = 0;
        Action createFixedCountEntityAction = new CreateFixedCountEntityAction(worldMap, countEntity);
        initActionList.add(createFixedCountEntityAction);
        Action makeMoveAction = new OnlyPredatorsMakeMoveWithSpeedAction(worldMap, tickCounter);
        turnActionList.add(makeMoveAction);

        for (Action action : initActionList) {
            action.perform();
            worldMapRender.renderMap();
            System.out.println();
        }

        while (tickCounter.getCurrentTick() < moveCount) {
            for (Action action : turnActionList) {
                action.perform();
                worldMapRender.renderMap();
                System.out.println();
            }
            tickCounter.next();
        }
    }

    public Optional<Entity> getEntityByCoordinates(Coordinates coordinates) {
        try {
            return Optional.of(worldMap.getEntityByCoordinates(coordinates));
        } catch (IllegalArgumentException iae) {
            return Optional.empty();
        }
    }

    public boolean areEmptyCoordinates(Coordinates coordinates) {
        return !worldMap.areBusy(coordinates);
    }

    public void startHerbivoreFirstWithoutSpeed(int moveCount) {
        int countEntity = 0;
        Action createFixedCountEntityAction = new CreateFixedCountEntityAction(worldMap, countEntity);
        initActionList.add(createFixedCountEntityAction);
        Action makeMoveAction = new HerbivoreFirstMakeMoveAction(worldMap);
        turnActionList.add(makeMoveAction);

        for (Action action : initActionList) {
            action.perform();
            worldMapRender.renderMap();
            System.out.println();
        }

        System.out.println("—————————— S T A R T ——————————");

        while (moveCount-- > 0) {
            for (Action action : turnActionList) {
                action.perform();
                worldMapRender.renderMap();
                System.out.println();
            }
            System.out.println("—————————— END MOVE ——————————");
        }
    }

    public void startHerbivoreFirst(int moveCount) {
        int countEntity = 0;
        Action createFixedCountEntityAction = new CreateFixedCountEntityAction(worldMap, countEntity);
        initActionList.add(createFixedCountEntityAction);
        Action makeMoveAction = new HerbivoreFirstMakeMoveWithSpeedAction(worldMap, tickCounter);
        turnActionList.add(makeMoveAction);

        for (Action action : initActionList) {
            action.perform();
            worldMapRender.renderMap();
            System.out.println();
        }

        System.out.println("—————————— S T A R T ——————————");

        while (tickCounter.getCurrentTick() < moveCount) {
            for (Action action : turnActionList) {
                action.perform();
                worldMapRender.renderMap();
                System.out.println();
            }
            tickCounter.next();
            System.out.println("—————————— END MOVE ——————————");
        }
    }

    public void startHerbivoresOnlyWithoutSpeed(int moveCount) {
        int countEntity = 0;
        Action createFixedCountEntityAction = new CreateFixedCountEntityAction(worldMap, countEntity);
        initActionList.add(createFixedCountEntityAction);
        Action makeMoveAction = new OnlyHerbivoresMakeMoveAction(worldMap);
        turnActionList.add(makeMoveAction);

        for (Action action : initActionList) {
            action.perform();
            worldMapRender.renderMap();
            System.out.println();
        }

        while (moveCount-- > 0) {
            for (Action action : turnActionList) {
                action.perform();
                worldMapRender.renderMap();
                System.out.println();
            }
        }
    }

    public void startHerbivoresOnly(int moveCount) {
        int countEntity = 0;
        Action createFixedCountEntityAction = new CreateFixedCountEntityAction(worldMap, countEntity);
        initActionList.add(createFixedCountEntityAction);
        Action makeMoveAction = new OnlyHerbivoresMakeMoveWithSpeedAction(worldMap, tickCounter);
        turnActionList.add(makeMoveAction);

        for (Action action : initActionList) {
            action.perform();
            worldMapRender.renderMap();
            System.out.println();
        }

        while (tickCounter.getCurrentTick() < moveCount) {
            for (Action action : turnActionList) {
                action.perform();
                worldMapRender.renderMap();
                System.out.println();
            }
            tickCounter.next();
        }
    }

    public void startWithGrassGrow() {
        Action createFixedCountEntityAction = new CreateFixedCountEntityAction(worldMap, 0);
        initActionList.add(createFixedCountEntityAction);
        Action makeMoveAction = new MakeMoveWithSpeedAction(worldMap, tickCounter);
        turnActionList.add(makeMoveAction);
        Action growGrassAction = new GrassGrowingAction(worldMap, tickCounter);
        turnActionList.add(growGrassAction);

        for (Action action : initActionList) {
            action.perform();
        }

        System.out.println();

        int moveCount = 360;
        while (tickCounter.getCurrentTick() < moveCount) {
            System.out.printf("[move: %d]\n", tickCounter.getCurrentTick());
            for (Action action : turnActionList) {
                action.perform();
            }
            worldMapRender.renderMap();
            tickCounter.next();
        }
    }
}
