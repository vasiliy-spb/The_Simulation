package bio.world.render;

public class ConsoleEntityIcons {
    public static final String HERBIVORE_ICON = "\uD83D\uDC07";
    public static final String PREDATOR_ICON = "\uD83D\uDC05";
    public static final String HUNTSMEN_ICON = "\uD83E\uDD20";
    public static final String GRASS_ICON = "☘\uFE0F";
    public static final String ROCK_ICON = "\uD83D\uDDFF";
    public static final String TREE_ICON = "\uD83C\uDF32";
    public static final String FLASH_ICON = "\uD83D\uDCA5";
    private static final String GREEN_COLOR = "\033[0;32m";
    public static final String RED_COLOR = "\033[0;31m";
    private static final String RESET_COLOR = "\033[0m";
    public static final String EMPTY_CELL = GREEN_COLOR + " .." + RESET_COLOR;
    public static final String EMPTY_CELL_TRIM = GREEN_COLOR + ".." + RESET_COLOR;
    public static final String TRAP_EMPTY_ICON = RED_COLOR + "[_]" + RESET_COLOR; // [_]
    public static final String TRAP_OPEN_ICON = RED_COLOR + "[" + RESET_COLOR; // [
    public static final String TRAP_CLOSE_ICON = RED_COLOR + "]" + RESET_COLOR; // ]
}
