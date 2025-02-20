package bio.world.factories;

import bio.world.WorldMap;
import bio.world.dialogs.Dialog;
import bio.world.dialogs.IntegerMinMaxDialog;

import java.util.Random;

public class WorldMapFactory {
    private static final int MIN_HEIGHT = 5;
    private static final int MAX_HEIGHT = 35;
    private static final int MIN_WIDTH = 5;
    private static final int MAX_WIDTH = 50;

    public static WorldMap getRandomWorldMap() {
        Random random = new Random();
        int height = random.nextInt(3, 10);
        int width = random.nextInt(3, 10);
        return new WorldMap(height, width);
    }

    public static WorldMap getInstance(int height, int width) {
        return new WorldMap(height, width);
    }

    public static WorldMap createWorldMapWithUserParams() {
        String askHeightMessage = "Введите высоту карты (%d - %d): ".formatted(MIN_HEIGHT, MAX_HEIGHT);
        String askWidthMessage = "Введите ширину карты: (%d - %d)".formatted(MIN_WIDTH, MAX_WIDTH);
        String errorMessage = "Неправильный ввод.";
        int height = askIntegerParams(askHeightMessage, errorMessage, MIN_HEIGHT, MAX_HEIGHT);
        int width = askIntegerParams(askWidthMessage, errorMessage, MIN_WIDTH, MAX_WIDTH);
        return getInstance(height, width);
    }

    private static int askIntegerParams(String askMessage, String errorMessage, int minValue, int maxValue) {
        Dialog<Integer> integerDialog = new IntegerMinMaxDialog(askMessage, errorMessage, minValue, maxValue);
        return integerDialog.input();
    }
}
