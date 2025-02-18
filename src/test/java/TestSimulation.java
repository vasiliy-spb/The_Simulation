import bio.world.MoveCounter;
import bio.world.WorldMap;
import bio.world.actions.Action;
import bio.world.actions.CreateFixedCountEntityAction;
import bio.world.actions.MakeMoveAction;
import bio.world.render.ConsoleMapRender;
import bio.world.render.WorldMapRender;
import factories.WorldMapFactoryTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestSimulation {
    private final WorldMap worldMap;
    private final MoveCounter moveCounter;
    private final WorldMapRender worldMapRender;
    private final List<Action> actionList;

    public TestSimulation() {
        String worldMapTemplate = readWorldMapTemplate("src/test/java/factories/worldMap_templates/template01.txt");
        System.out.println("worldMapTemplate: \n" + worldMapTemplate);
        this.worldMap = WorldMapFactoryTest.createWorldMapByTemplate(worldMapTemplate);
        this.moveCounter = new MoveCounter();
        this.worldMapRender = new ConsoleMapRender(worldMap);
        this.actionList = new ArrayList<>();
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
        actionList.add(createFixedCountEntityAction);
        Action makeMoveAction = new MakeMoveAction(worldMap);
        actionList.add(makeMoveAction);

        for (Action action : actionList) {
            action.perform();
            worldMapRender.renderMap();
            System.out.println();
        }
    }
}
