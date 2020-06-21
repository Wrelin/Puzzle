package wrell.app.utility.side;

public class Side implements Cloneable {
    final private SideType type;
    private SideDirection direction;

    public Side(SideType type, SideDirection direction) {
        this.type = type;
        this.direction = direction;
    }

    public SideDirection getDirection() {
        return this.direction;
    }

    public SideType getType() {
        return this.type;
    }

    public void clockwiseDirection() {
        direction = direction.getClockwise();
    }

    public boolean isFit(Side side) {
        return this.direction == side.direction.getOpposite()
                && this.type == side.type.getPair();
    }

    @Override
    public String toString() {
        if (type.equals(SideType.CONCAVE) && direction.equals(SideDirection.NORTH)) {
            return "⍌";
        } else  if (type.equals(SideType.CONCAVE) && direction.equals(SideDirection.EAST)) {
            return "⍃";
        } else  if (type.equals(SideType.CONCAVE) && direction.equals(SideDirection.SOUTH)) {
            return "⍓";
        } else  if (type.equals(SideType.CONCAVE) && direction.equals(SideDirection.WEST)) {
            return "⍄";
        } else  if (type.equals(SideType.CONVEX) && direction.equals(SideDirection.NORTH)) {
            return "↑";
        } else  if (type.equals(SideType.CONVEX) && direction.equals(SideDirection.EAST)) {
            return "→";
        } else  if (type.equals(SideType.CONVEX) && direction.equals(SideDirection.SOUTH)) {
            return "↓";
        } else  if (type.equals(SideType.CONVEX) && direction.equals(SideDirection.WEST)) {
            return "←";
        } else  if (type.equals(SideType.BOARD) && direction.equals(SideDirection.NORTH)) {
            return "_";
        } else  if (type.equals(SideType.BOARD) && direction.equals(SideDirection.EAST)) {
            return "|";
        } else  if (type.equals(SideType.BOARD) && direction.equals(SideDirection.SOUTH)) {
            return "‾";
        } else  if (type.equals(SideType.BOARD) && direction.equals(SideDirection.WEST)) {
            return "|";
        }
        return "";
    }

    @Override
    public Side clone() {
        return new Side(type, direction);
    }
}
