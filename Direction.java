

public enum Direction {
    UP (0),
    RIGHT (1),
    DOWN (2),
    LEFT (3),
    STILL (4),
    
    UPRIGHT(5),
    DOWNRIGHT(6),
    DOWNLEFT (7),
    UPLEFT(8);
    
    
  

    public final static int nrMoves = 5;
    private final int intValue;
    public  final static Direction[] enumValues = Direction.values();

    Direction(int intValue) {
        this.intValue = intValue;
    }

}