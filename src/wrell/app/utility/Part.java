package wrell.app.utility;

import wrell.app.utility.interfaces.Validatable;
import wrell.app.utility.side.Side;
import wrell.app.utility.side.SideDirection;
import wrell.app.utility.side.SideType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Part implements Validatable, Cloneable {
    private static final int SIZES_PER_PART = 4;
    private static final int MAX_BOARD_SIZES = 2;
    private static int idCounter = 1;

    public int id;
    protected List<Side> sides;
    protected boolean isCorner;
    protected boolean isBoard;

    public static Part getByString(String string) {
        if (string.length() != SIZES_PER_PART)
            throw new RuntimeException("Invalid string length");

        var sides = new ArrayList<Side>();
        for (int i = 0; i < string.length(); i++) {
            var direction = sides.size() > 0
                    ? sides.get(sides.size() - 1).getDirection().getClockwise()
                    : SideDirection.getDefault();
            sides.add(new Side(SideType.getBySymbol(string.charAt(i)), direction));
        }
        return new Part(sides);
    }

    public Part(List<Side> sides) {
        this.sides = sides;
        setMeta();
        validate();
    }

    public Part(List<Side> sides, int id, boolean isCorner, boolean isBoard) {
        this.sides = sides;
        this.id = id;
        this.isCorner = isCorner;
        this.isBoard = isBoard;
    }

    protected void setMeta() {
        id = idCounter++;
        isCorner = isCorrectCorner();
        isBoard = (int) sides.stream().filter(side -> side.getType().equals(SideType.BOARD)).count() == 1;
    }

    private void validate() {
        if (sides.size() != SIZES_PER_PART)
            throw new RuntimeException("Invalid size count");
        if (getBordersCount() > MAX_BOARD_SIZES)
            throw new RuntimeException("Invalid board count");
        if (getBordersCount() == MAX_BOARD_SIZES && !isCorrectCorner())
            throw new RuntimeException("Invalid border positions");
        if (!sides.stream().map(Side::getDirection).collect(Collectors.toSet()).containsAll(SideDirection.getSet()))
            throw new RuntimeException("Invalid size directions");
    }

    public Side getSideByDirection(SideDirection direction) {
        return sides.stream().filter(side -> side.getDirection().equals(direction)).findFirst().get();
    }

    public boolean isCorner() {
        return isCorner;
    }

    public boolean isBoard() {
        return isBoard;
    }

    public int getBordersCount() {
        return getBorders().size();
    }

    public List<Side> getBorders() {
        return sides.stream().filter(side -> side.getType().equals(SideType.BOARD)).collect(Collectors.toList());
    }

    public int getSidesSum() {
        return sides.stream().reduce(0, (integer, side) -> integer + side.getType().getIndex(), Integer::sum);
    }

    public void clockwise() {
        sides.forEach(Side::clockwiseDirection);
    }

    public boolean isFitOnDirection(Part part, SideDirection direction) {
        return this.getSideByDirection(direction).isFit(part.getSideByDirection(direction.getOpposite()));
    }

    public boolean isBoardOnDirection(SideDirection direction) {
        return this.getSideByDirection(direction).getType().equals(SideType.BOARD);
    }

    @Override
    public Part clone() {
        return new Part(sides.stream().map(Side::clone).collect(Collectors.toList()), id, isCorner, isBoard);
    }

    protected boolean isCorrectCorner() {
        var boardSides = getBorders();
        return boardSides.size() == MAX_BOARD_SIZES
                && (boardSides.get(0).getDirection().getClockwise().equals(boardSides.get(1).getDirection())
                || boardSides.get(1).getDirection().getClockwise().equals(boardSides.get(0).getDirection()));
    }
}
