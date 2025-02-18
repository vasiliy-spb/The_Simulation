package bio.world;

public record Coordinates(int row, int column) {
    public void print() {
        System.out.printf("[%d - %d]%n", row, column);
    }

    public String getPosition() {
        return "[%d - %d]%n".formatted(row, column);
    }

    @Override
    public String toString() {
        return "[%d - %d]".formatted(row, column);
//        return "Coordinates{" +
//                "row=" + row +
//                ", column=" + column +
//                '}';
    }
}
