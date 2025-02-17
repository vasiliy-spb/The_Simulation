package bio.world.render;

public enum Pictures {
    RABBIT("\uD83D\uDC07"), // 🐇
    TIGER("\uD83D\uDC05"), // 🐅
    TREE("\uD83C\uDF33"), // 🌳
    GRASS("☘\uFE0F"), // ☘️
    STONE("\uD83D\uDDFF"), // 🗿
    SPRUCE_TREE("\uD83C\uDF32"); // 🌲
    private final String s;

    Pictures(String s) {
        this.s = s;
    }

    public String getValue() {
        return this.s;
    }
}
