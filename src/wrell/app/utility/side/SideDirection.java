package wrell.app.utility.side;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum  SideDirection {
    NORTH, EAST, SOUTH, WEST;

    public static Set<SideDirection> getSet() {
        return new HashSet<>(Arrays.asList(SideDirection.values()));
    }

    public static SideDirection getDefault() {
        return NORTH;
    }

    public SideDirection getClockwise() {
        return switch (this) {
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
    }

    public SideDirection getOpposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case EAST -> WEST;
            case SOUTH -> NORTH;
            case WEST -> EAST;
        };
    }
}
