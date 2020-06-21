package wrell.app.utility.side;

public enum SideType {
    BOARD(0),
    CONVEX(1),
    CONCAVE(-1);

    final private int index;

    public static SideType getBySymbol(char symbol) {
        return switch (symbol) {
            case '|' -> BOARD;
            case ')' -> CONVEX;
            case '(' -> CONCAVE;
            default -> throw new RuntimeException("Incorrect symbol");
        };
    }

    SideType(int index) {
        this.index = index;
    }

    public SideType getPair() {
        return switch (this) {
            case BOARD -> null;
            case CONVEX -> CONCAVE;
            case CONCAVE -> CONVEX;
        };
    }

    public int getIndex() {
        return index;
    }
}
