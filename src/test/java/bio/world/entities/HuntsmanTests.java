package bio.world.entities;

import bio.world.TestSimulation;
import bio.world.entities.regular.Herbivore;
import bio.world.entities.regular.Huntsman;
import bio.world.entities.regular.Predator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HuntsmanTests {

    @Test
    @DisplayName("Huntsmen hunt to Predator early Herbivore")
    public void checkTestcase01() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template23.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 200;

        Set<Herbivore> herbivores = new HashSet<>();
        Set<Predator> predators = new HashSet<>();

        int side = 13;
        for (int row = 0; row < side; row++) {
            for (int column = 0; column < side; column++) {
                Coordinates coordinates = new Coordinates(row, column);
                Optional<Entity> entityContainer = testSimulation.getEntityByCoordinates(coordinates);
                if (entityContainer.isEmpty()) {
                    continue;
                }
                Entity entity = entityContainer.get();
                if (entity instanceof Herbivore herbivore) {
                    herbivores.add(herbivore);
                }
                if (entity instanceof Predator predator) {
                    predators.add(predator);
                }
            }
        }

        testSimulation.startHuntsmenOnly(moveCount);

        boolean allHerbivoresStillAlive = true;
        for (Herbivore herbivore : herbivores) {
            allHerbivoresStillAlive &= herbivore.isAlive();
        }

        assertTrue(allHerbivoresStillAlive);
    }

    @Test
    @DisplayName("The huntsman tramples the grass when make random move")
    public void checkTestcase19() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template22.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 2;
        Coordinates huntsmanStartCoordinates = new Coordinates(2, 2);
        Optional<Entity> entityContainer = testSimulation.getEntityByCoordinates(huntsmanStartCoordinates);
        Huntsman huntsman = (Huntsman) entityContainer.get();

        testSimulation.startHuntsmenOnly(moveCount);

        Coordinates huntsmanFinishCoordinates = huntsman.getCoordinates();
        boolean huntsmanMoved = !huntsmanStartCoordinates.equals(huntsmanFinishCoordinates);

        assertTrue(huntsmanMoved);
    }
}
