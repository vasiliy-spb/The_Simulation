package bio.world.entities;

import bio.world.TestSimulation;
import bio.world.entities.statical.trap.Trap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrapTests {

    @Test
    @DisplayName("A predator find a target when the trap disappears")
    public void checkTestcase01() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template28.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 80;

        Trap trap1 = (Trap) testSimulation.getEntityByCoordinates(new Coordinates(3, 3)).get();
        Trap trap2 = (Trap) testSimulation.getEntityByCoordinates(new Coordinates(3, 4)).get();
        Trap trap3 = (Trap) testSimulation.getEntityByCoordinates(new Coordinates(3, 5)).get();
        Trap trap4 = (Trap) testSimulation.getEntityByCoordinates(new Coordinates(4, 3)).get();
        Trap trap5 = (Trap) testSimulation.getEntityByCoordinates(new Coordinates(4, 5)).get();
        Trap trap6 = (Trap) testSimulation.getEntityByCoordinates(new Coordinates(5, 3)).get();
        Trap trap7 = (Trap) testSimulation.getEntityByCoordinates(new Coordinates(5, 4)).get();
        Trap trap8 = (Trap) testSimulation.getEntityByCoordinates(new Coordinates(5, 5)).get();
        Set<Trap> traps = Set.of(trap1, trap2, trap3, trap4, trap5, trap6, trap7, trap8);

        testSimulation.startPredatorsOnly(moveCount);

        boolean onePredatorPerTrapForAllTraps = true;
        for (Trap trap : traps) {
            onePredatorPerTrapForAllTraps &= trap.hasCapturedCreature();
        }

        assertTrue(onePredatorPerTrapForAllTraps);
    }
}
