package bio.world;

import bio.world.actions.*;
import bio.world.entities.Entity;
import bio.world.entities.Herbivore;
import bio.world.render.ConsoleMapRender;
import bio.world.render.WorldMapRender;
import bio.world.factories.WorldMapFactoryTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TestSimulation {
    private final WorldMap worldMap;
    private final MoveCounter moveCounter;
    private final WorldMapRender worldMapRender;
    private final List<Action> initActionList;
    private final List<Action> turnActionList;

    public TestSimulation(String templateFilePath) {
        String worldMapTemplate = readWorldMapTemplate(templateFilePath);
        System.out.println("worldMapTemplate: \n" + worldMapTemplate);
        this.worldMap = WorldMapFactoryTest.createWorldMapByTemplate(worldMapTemplate);
        this.moveCounter = new MoveCounter();
        this.worldMapRender = new ConsoleMapRender(worldMap);
        this.initActionList = new ArrayList<>();
        this.turnActionList = new ArrayList<>();
    }

    private String readWorldMapTemplate(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            StringBuilder template = new StringBuilder();
            while (reader.ready()) {
                template.append(reader.readLine()).append("\n");
            }
            return template.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
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

    public void startPredatorsOnly() {
        startPredatorsOnly(1);
    }
    public void startPredatorsOnly(int moveCount) {
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

    public Optional<Entity> getEntityByCoordinates(Coordinates coordinates) {
        return Optional.ofNullable(worldMap.getEntityByCoordinates(coordinates));
    }

    public boolean areEmptyCoordinates(Coordinates coordinates) {
        return !worldMap.areBusy(coordinates);
    }

    public void startHerbivoreFirst(int moveCount) {
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
}
