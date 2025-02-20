package bio.world;

import bio.world.actions.*;
import bio.world.dialogs.Dialog;
import bio.world.dialogs.IntegerMinMaxDialog;
import bio.world.entities.Herbivore;
import bio.world.factories.*;
import bio.world.menu.Menu;
import bio.world.menu.MenuItems;
import bio.world.render.ConsoleMapRender;
import bio.world.render.WorldMapRender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private final WorldMap worldMap;
    private final TickCounter tickCounter;
    private final WorldMapRender worldMapRender;
    private final List<Action> initActionList;
    private final List<Action> turnActionList;
    private final PauseHandler pauseHandler;
    private int movesDelay;

    public Simulation() {
        this.worldMap = WorldMapFactory.createWorldMapWithUserParams();
        this.tickCounter = new TickCounter();
        this.worldMapRender = new ConsoleMapRender(worldMap);
        this.initActionList = new ArrayList<>();
        this.turnActionList = new ArrayList<>();
        this.pauseHandler = new PauseHandler();
        this.movesDelay = 0;
    }

    public void start() {
        init();
        while (!isGameOver()) {
            try {
                pauseHandler.checkPause();
                if (pauseHandler.isPauseOn()) {
                    if (shouldInterrupt()) {
                        return;
                    }
                }
                Thread.sleep(movesDelay);
                nextTurn();
            } catch (IOException | InterruptedException ignored) {
            }
        }
    }

    private void init() {
        Action createFixedCountEntityAction = new CreateCustomCountEntityAction(worldMap);
        initActionList.add(createFixedCountEntityAction);
        Action makeMoveAction = new MakeMoveWithSpeedAction(worldMap, tickCounter);
        turnActionList.add(makeMoveAction);

        for (Action action : initActionList) {
            action.perform();
        }

        this.movesDelay = askMovesDelay();
    }

    private boolean shouldInterrupt() {
        Menu pauseMenu = MenuFactory.createPauseMenu();
        pauseMenu.showTitle();
        MenuItems selectedMenuItem = pauseMenu.selectMenuItem();
        switch (selectedMenuItem) {
            case CONTINUE -> pauseHandler.togglePause();
            case CHANGE_MOVES_DELAY -> {
                movesDelay = askMovesDelay();
                pauseHandler.togglePause();
            }
            case EXIT -> {
                return true;
            }
        }
        return false;
    }

    private void nextTurn() {
        for (Action action : turnActionList) {
            action.perform();
            worldMapRender.renderMap();
            System.out.println();
        }
        tickCounter.next();
    }

    private boolean isGameOver() {
        return worldMap.getCreatures()
                .stream()
                .noneMatch(c -> c instanceof Herbivore);
    }

    private int askMovesDelay() {
        String title = """
                    Выберите скорость симуляции:
                    1 — медленная (1 ход/сек)
                    2 — быстрее (2 хода/сек)
                    3 — средняя (5 хода/сек)
                    4 — быстрая (10 ходов/сек)
                    5 — максимальная (20 ходов/сек)
                """;
        String errorMessage = "Неправильный ввод.";
        int minValue = 1;
        int maxValue = 5;
        Dialog<Integer> integerDialog = new IntegerMinMaxDialog(title, errorMessage, minValue, maxValue);
        int selectedSpeed = integerDialog.input();
        return switch (selectedSpeed) {
            case 1 -> 1000;
            case 2 -> 500;
            case 3 -> 200;
            case 4 -> 100;
            case 5 -> 50;
            default -> throw new IllegalStateException("Unexpected value: " + selectedSpeed);
        };
    }
}
