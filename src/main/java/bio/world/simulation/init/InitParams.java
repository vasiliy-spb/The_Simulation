package bio.world.simulation.init;

public record InitParams(
        int height,
        int width,
        int countTrees,
        int countRocks,
        int countGrasses,
        int countHerbivores,
        int countPredators) {

}
