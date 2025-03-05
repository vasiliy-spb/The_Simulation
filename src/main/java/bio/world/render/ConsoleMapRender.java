package bio.world.render;

import bio.world.entities.Coordinates;
import bio.world.entities.regular.Grass;
import bio.world.entities.regular.Herbivore;
import bio.world.entities.regular.Huntsman;
import bio.world.entities.regular.Predator;
import bio.world.entities.statical.Rock;
import bio.world.entities.statical.Tree;
import bio.world.entities.statical.trap.Trap;
import bio.world.entities.temporary.Flash;
import bio.world.map.WorldMap;
import bio.world.entities.*;

import java.util.Map;
import java.util.Set;

import static bio.world.render.ConsoleEntityIcons.*;

public class ConsoleMapRender implements WorldMapRender {
    private static final String DISPLAY_INFO = "ENTER — Пауза";
    private static final Map<Class<? extends Entity>, String> ENTITY_PICTURES = Map.of(
            Tree.class, TREE_ICON,
            Rock.class, ROCK_ICON,
            Grass.class, GRASS_ICON,
            Herbivore.class, HERBIVORE_ICON,
            Predator.class, PREDATOR_ICON,
            Huntsman.class, HUNTSMEN_ICON,
            Flash.class, FLASH_ICON,
            Trap.class, TRAP_EMPTY_ICON
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
            Entity prevEntity = null;
            for (int j = 0; j < width; j++) {
                Coordinates coordinates = new Coordinates(i, j);

                if (!busyCoordinates.contains(coordinates)) {
                    if (prevEntity instanceof Trap trap && trap.hasCapturedCreature()) {
                        worldMapRepresentation.append(EMPTY_CELL_TRIM);
                    } else {
                        worldMapRepresentation.append(EMPTY_CELL);
                    }
                    prevEntity = null;
                    continue;
                }

                Entity entity = worldMap.getEntityByCoordinates(coordinates);
                prevEntity = entity;

                String picture = getEntityIcon(entity);
                if (entity instanceof Trap) {
                    worldMapRepresentation.append(picture);
                } else {
                    worldMapRepresentation.append(String.format(" %2s", picture));
                }

            }
            worldMapRepresentation.append("\n");
        }

        worldMapRepresentation.append(DISPLAY_INFO).append("\n");
        System.out.println(worldMapRepresentation);
    }

    private String getEntityIcon(Entity entity) {
        if (entity instanceof Trap trap) {
            if (trap.hasCapturedCreature()) {
                Class<? extends Entity> type = trap.getCapturedCreature().get().getClass();
                return TRAP_OPEN_ICON + ENTITY_PICTURES.getOrDefault(type, "") + TRAP_CLOSE_ICON;
            }
        }
        return ENTITY_PICTURES.getOrDefault(entity.getClass(), "");
    }
}
