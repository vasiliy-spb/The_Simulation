package bio.world.factories;

import bio.world.simulation.init.InitParams;

import java.util.Random;

public class InitParamsFactory {
    private final Random random;
    private static final int MIN_NUMBER_OF_ENTITY = 1;

    public InitParamsFactory() {
        this.random = new Random();
    }

    public InitParams generateRandomParams() {
        int height = generateRandomInt(5, 35);
        int width = generateRandomInt(5, 50);
        int availableCountEntities = height * width;
        int availableCountStaticEntities = availableCountEntities / 2;
        int minSummaryCountOfOtherEntities = 4;

        int countTrees = generateRandomInt(MIN_NUMBER_OF_ENTITY, availableCountStaticEntities);
        availableCountEntities -= countTrees;
        availableCountStaticEntities -= countTrees;
        minSummaryCountOfOtherEntities--;

        int countRocks = generateRandomInt(MIN_NUMBER_OF_ENTITY, availableCountStaticEntities);
        availableCountEntities -= countRocks;
        availableCountStaticEntities -= countRocks;
        minSummaryCountOfOtherEntities--;

        int countGrasses = generateRandomInt(MIN_NUMBER_OF_ENTITY, availableCountStaticEntities);
        availableCountEntities -= countGrasses;
        minSummaryCountOfOtherEntities--;

        int countHerbivores = generateRandomInt(MIN_NUMBER_OF_ENTITY, availableCountEntities - minSummaryCountOfOtherEntities);
        availableCountEntities -= countHerbivores;
        minSummaryCountOfOtherEntities--;

        int countPredators = generateRandomInt(MIN_NUMBER_OF_ENTITY, availableCountEntities - minSummaryCountOfOtherEntities);

        InitParams initParams = new InitParams(height, width, countTrees, countRocks, countGrasses, countHerbivores, countPredators);
        return initParams;
    }

    private int generateRandomInt(int min, int max) {
        return random.nextInt(min, max + 1);
    }
}
