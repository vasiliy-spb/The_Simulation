package bio.world.factories;

import bio.world.menu.MainMenu;
import bio.world.menu.PauseMenu;
import bio.world.menu.StartMenu;

public class MenuFactory {
    public static StartMenu createStartMenu() {
        String startTitleMessage = """
                ===============================================================================================
                                                       С И М У Л Я Ц И Я
                ===============================================================================================
                """;
        String startSelectMessage = """
                Выберите пункт меню:
                1 — Играть
                2 — Выйти
                """;
        String startErrorMessage = "Неправильный ввод.";
        int minStartMenuItem = 1;
        int maxStartMenuItem = 2;
        StartMenu startMenu = new StartMenu(startTitleMessage, startSelectMessage, startErrorMessage, minStartMenuItem, maxStartMenuItem);
        return startMenu;
    }

    public static MainMenu createMainMenu() {
        String mainTitleMessage = "СИМУЛЯЦИЯ ЗАВЕРШЕНА";
        String mainSelectMessage = """
                Выберите пункт меню:
                1 — Играть ещё раз
                2 — Выйти
                """;
        String mainErrorMessage = "Неправильный ввод.";
        int minMainMenuItem = 1;
        int maxMainMenuItem = 2;
        MainMenu mainMenu = new MainMenu(mainTitleMessage, mainSelectMessage, mainErrorMessage, minMainMenuItem, maxMainMenuItem);
        return mainMenu;
    }

    public static PauseMenu createPauseMenu() {
        String mainTitleMessage =
                "========================================== P A U S E ==========================================";
        String mainSelectMessage = """
                ENTER — Продолжить
                1 — Изменить скорость
                2 — Прервать
                """;
        String mainErrorMessage = "Неправильный ввод.";
        int minMainMenuItem = 1;
        int maxMainMenuItem = 2;
        PauseMenu pauseMenu = new PauseMenu(mainTitleMessage, mainSelectMessage, mainErrorMessage, minMainMenuItem, maxMainMenuItem);
        return pauseMenu;
    }
}
