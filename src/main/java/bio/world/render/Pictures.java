package bio.world.render;

public enum Pictures {
    RABBIT ("\uD83D\uDC07"), // 🐇
    TIGER ("\uD83D\uDC05"), // 🐅
    TREE ("\uD83C\uDF33"), // 🌳
    GRASS ("☘\uFE0F"), // ☘️
    STONE ("\uD83D\uDDFF"), // 🗿
    HUNTSMAN("\uD83E\uDD20"), // 🤠
    CAMP("\uD83C\uDFD5\uFE0F"), // 🏕️
    FLASH("\uD83D\uDCA5"), // 💥
    SPRUCE_TREE("\uD83C\uDF32"), // 🌲
    TRAP_EMPTY("\033[0;91m" + "[]" + "\033[0m"), // []
//    TRAP_OPEN("\033[0;91m" + "[" + "\033[0m"), // [
//    TRAP_CLOSE("\033[0;91m" + "]" + "\033[0m"); // ]
    TRAP_OPEN("\033[4;31m"), // RED UNDERLINED
    TRAP_CLOSE("\033[0m"); // RESET


    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String RED_BACKGROUND = "\033[41m";    // RED
    public static final String RED_UNDERLINED = "\033[4;31m";
//    public static final String RED_BRIGHT = "\033[0;91m";    // RED
//    public static final String RESET = "\033[0m";  // Text Reset
//    public static final String RED = "\033[0;31m";     // RED

    String s;
    Pictures(String s) {
        this.s = s;
    }
    String getValue() {
        return this.s;
    }
}

/*
🪨🐇🐅🐆🥷🏼
🏹
 */