package bio.world.render;

import bio.world.entities.Coordinates;
import bio.world.map.WorldMap;
import bio.world.entities.*;

import java.util.Map;
import java.util.Set;

import static bio.world.render.ConsoleEntityIcons.*;

public class ConsoleMapRender implements WorldMapRender {
    private static final Map<Class<? extends Entity>, String> ENTITY_PICTURES = Map.of(
            Tree.class, TREE_ICON,
            Rock.class, ROCK_ICON,
            Grass.class, GRASS_ICON,
            Herbivore.class, HERBIVORE_ICON,
            Predator.class, PREDATOR_ICON
    );
    private final WorldMap worldMap;

    public ConsoleMapRender(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    @Override
    public void renderMap() {
        int height = worldMap.getHeight();
        int width = worldMap.getWidth();
        Set<Coordinates> busyCoordinates = worldMap.getBusyCoordinates();
        StringBuilder worldMapRepresentation = new StringBuilder();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Coordinates coordinates = new Coordinates(i, j);

                if (!busyCoordinates.contains(coordinates)) {
                    worldMapRepresentation.append(EMPTY_CELL);
                    continue;
                }

                Entity entity = worldMap.getEntityByCoordinates(coordinates);

                String picture = getEntityIcon(entity);

                worldMapRepresentation.append(String.format(" %2s", picture));
            }
            worldMapRepresentation.append("\n");
        }

        System.out.println(worldMapRepresentation);
    }

    private String getEntityIcon(Entity entity) {
        return ENTITY_PICTURES.getOrDefault(entity.getClass(), "");
    }
}
