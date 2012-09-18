

public enum Direction {
    UP (0),
    RIGHT (1),
    DOWN (2),
    LEFT (3),
    STILL (4);

    private final int intValue;
    private final Direction[] enumValues = Direction.values();

    Direction(int intValue) {
        this.intValue = intValue;
    }

}