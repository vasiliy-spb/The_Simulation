package bio.world.simulation;

import bio.world.entities.Entity;
import bio.world.entities.regular.Creature;
import bio.world.simulation.init.InitParams;
import bio.world.simulation.init.InitParamsHandler;
import bio.world.map.WorldMap;
import bio.world.actions.*;
import bio.world.dialogs.Dialog;
import bio.world.dialogs.IntegerMinMaxDialog;
import bio.world.factories.*;
import bio.world.menu.Menu;
import bio.world.menu.MenuItems;
import bio.world.render.ConsoleMapRender;
import bio.world.render.WorldMapRender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Simulation {
    private WorldMap worldMap;
    private final TickCounter tickCounter;
    private final WorldMapRender worldMapRender;
    private final List<Action> initActionList;
    private final List<Action> turnActionList;
    private final PauseHandler pauseHandler;
    private int movesDelay;
    private final InitParamsHandler initParamsHandler;

    public Simulation(InitParamsHandler initParamsHandler) {
        this.initParamsHandler = initParamsHandler;
        this.tickCounter = new TickCounter();
        this.initActionList = new ArrayList<>();
        this.turnActionList = new ArrayList<>();
        this.pauseHandler = new PauseHandler();
        this.movesDelay = 0;

        if (initParamsHandler.hasSavedData()) {
            createWorldMap();
            initWithSavedParams();
        } else {
            this.worldMap = WorldMapFactory.createWorldMapWithUserParams();
            init();
        }

        this.worldMapRender = new ConsoleMapRender(worldMap);
    }

    private void createWorldMap() {
        Optional<InitParams> initParamsContainer = initParamsHandler.getSavedInitParams();

        if (initParamsContainer.isEmpty()) {
            this.worldMap = WorldMapFactory.createWorldMapWithUserParams();
            return;
        }

        InitParams initParams = initParamsContainer.get();
        this.worldMap = WorldMapFactory.createWorldMapWithParams(initParams);
    }

    private void initWithSavedParams() {
        Action createSavedCountEntityAction = new CreateSavedCountEntityAction(worldMap, initParamsHandler);
        initActionList.add(createSavedCountEntityAction);

        Action removeTemporaryEntityAction = new RemoveTemporaryEntityAction(worldMap, tickCounter);
        Action makeMoveAction = new MakeMoveAction(worldMap, tickCounter);
        Action createFlashAction = new CreateFlashAction(worldMap, tickCounter);
        Action growGrassAction = new GrassGrowingAction(worldMap, tickCounter);

        turnActionList.add(removeTemporaryEntityAction);
        turnActionList.add(makeMoveAction);
        turnActionList.add(createFlashAction);
        turnActionList.add(growGrassAction);

        for (Action action : initActionList) {
            action.perform();
        }

        this.movesDelay = askMovesDelay();
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

        return getMoveDelayForSpeed(selectedSpeed);
    }

    private int getMoveDelayForSpeed(int selectedSpeed) {
        return switch (selectedSpeed) {
            case 1 -> 1000;
            case 2 -> 500;
            case 3 -> 200;
            case 4 -> 100;
            case 5 -> 50;
            default -> throw new IllegalStateException("Unexpected value: " + selectedSpeed);
        };
    }

    private void init() {
        Action createFixedCountEntityAction = new CreateCustomCountEntityAction(worldMap, initParamsHandler);
        initActionList.add(createFixedCountEntityAction);

        Action removeTemporaryEntityAction = new RemoveTemporaryEntityAction(worldMap, tickCounter);
        Action makeMoveAction = new MakeMoveAction(worldMap, tickCounter);
        Action createFlashAction = new CreateFlashAction(worldMap, tickCounter);
        Action growGrassAction = new GrassGrowingAction(worldMap, tickCounter);

        turnActionList.add(removeTemporaryEntityAction);
        turnActionList.add(makeMoveAction);
        turnActionList.add(createFlashAction);
        turnActionList.add(growGrassAction);

        for (Action action : initActionList) {
            action.perform();
        }

        this.movesDelay = askMovesDelay();
    }

    public void start() {
        worldMapRender.renderMap();

        try {
            while (!isGameOver()) {
                pauseHandler.checkPause();

                if (pauseHandler.isPauseOn()) {
                    if (shouldInterrupt()) {
                        return;
                    }
                }

                Thread.sleep(movesDelay);
                nextTurn();

            }
        } catch (IOException | InterruptedException ignored) {
        }
    }

    private boolean isGameOver() {
        List<Entity> entities = worldMap.getAllEntities();

        for (Entity entity : entities) {
            if (entity instanceof Creature) {
                return false;
            }
        }
        return true;
    }

    private boolean shouldInterrupt() {
        MenuItems selectedMenuItem = askPauseMenuItem();

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

    private static MenuItems askPauseMenuItem() {
        Menu pauseMenu = MenuFactory.createPauseMenu();
        pauseMenu.showTitle();
        MenuItems selectedMenuItem = pauseMenu.selectMenuItem();
        return selectedMenuItem;
    }

    private void nextTurn() {
        for (Action action : turnActionList) {
            action.perform();
        }
        worldMapRender.renderMap();
        tickCounter.next();
    }
}
