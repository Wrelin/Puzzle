package wrell.app.utility;

import wrell.app.utility.interfaces.Validatable;
import wrell.app.utility.side.SideDirection;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class Puzzle implements Validatable {
    private static final int NEED_CORNERS = 4;
    protected List<Part> parts;
    private List<Stack<Part>> stacks;
    private Part[][] grid;
    protected int perimeter;
    protected int area;
    protected int width;
    protected int height;
    final private List<String> variants = new ArrayList<>();

    public Puzzle(List<Part> parts) {
        this.parts = parts;
        this.setMeta();
        this.validate();
    }

    public static Puzzle getByArray(String[] array) {
        if (array.length < NEED_CORNERS || BigInteger.valueOf(array.length).isProbablePrime(1))
            throw new RuntimeException("Incorrect array size");
        var parts = Arrays.stream(array)
                .map(Part::getByString)
                .collect(Collectors.toList());
        return new Puzzle(parts);

    }

    private void validate() {
        if (parts.isEmpty() || BigInteger.valueOf(parts.size()).isProbablePrime(1))
            throw new RuntimeException("Incorrect parts count");
        if (parts.stream().filter(Part::isCorner).count() != NEED_CORNERS)
            throw new RuntimeException("Incorrect corners count");
        if (width <= 0)
            throw new RuntimeException("Incorrect borders count, width not valid");
        if (height <= 0)
            throw new RuntimeException("Incorrect borders count, height not valid");
        if (width * height != area)
            throw new RuntimeException("Incorrect borders count, area not valid");
        if (parts.stream().reduce(0, (integer, part) -> integer + part.getSidesSum(), Integer::sum) != 0)
            throw new RuntimeException("Incorrect convex and concave count");
    }

    public boolean check() {
        stacks = new ArrayList<>();
        first:
        for (Part part: parts) {
            for (Stack<Part> stack: stacks) {
                if (stack.peek().equals(part)) {
                    stack.push(part);
                    continue first;
                }
            }
            var newStack = new Stack<Part>();
            newStack.push(part);
            stacks.add(newStack);
        }
        setToGrid(0, 0);
        return !variants.isEmpty();
    }

    public List<String> getVariants(){
        return variants;
    }

    protected void setToGrid(int x, int y) {
        for (Stack<Part> stack : stacks) {
            if (stack.isEmpty()) continue;

            var part = stack.peek();
            for (SideDirection direction : SideDirection.values()) {
                if (checkNeighborsFit(x, y, part)) {
                    grid[y][x] = part;
                    if (x + 1 == width && y + 1 == height) {
                        variants.add(drawGrid());
                        return;
                    }
                    stack.pop();
                    setToGrid(x + 1 < width ? x + 1 : 0, x + 1 < width ? y : y + 1);
                    stack.push(part);
                    if (x == 0 && y == 0) return;
                }
                part.clockwise();
            }
        }
    }

    protected boolean checkNeighborsFit(int x, int y, Part part) {
        if (x == 0) {
            if (!part.isBoardOnDirection(SideDirection.WEST))
                return false;
        } else if (x == width - 1) {
            if (!part.isBoardOnDirection(SideDirection.EAST))
                return false;
            if (!grid[y][x - 1].isFitOnDirection(part, SideDirection.EAST))
                return false;
        } else {
            if (part.isCorner || !grid[y][x - 1].isFitOnDirection(part, SideDirection.EAST))
                return false;
        }
        if (y == 0) {
            if (!part.isBoardOnDirection(SideDirection.NORTH))
                return false;
        } else if (y == height - 1) {
            if (!part.isBoardOnDirection(SideDirection.SOUTH))
                return false;
            if (!grid[y - 1][x].isFitOnDirection(part, SideDirection.SOUTH))
                return false;
        } else {
            if (part.isCorner || !grid[y - 1][x].isFitOnDirection(part, SideDirection.SOUTH))
                return false;
        }
        return true;
    }

    protected void setMeta() {
        area = parts.size();
        perimeter = parts.stream().reduce(0, (integer, part) -> integer + part.getBordersCount(), Integer::sum);
        int discriminantSqrt = (int) Math.sqrt(perimeter * perimeter - 16 * area);
        width = Integer.max((perimeter - discriminantSqrt) / 4, (perimeter + discriminantSqrt) / 4);
        height = (perimeter - 2 * width) / 2;
        grid = new Part[height][width];
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Puzzle{");
        sb.append("perimeter=").append(perimeter);
        sb.append(", area=").append(area);
        sb.append(", width=").append(width);
        sb.append(", height=").append(height);
        sb.append(", \nparts=").append(parts);
        sb.append('}');
        return sb.toString();
    }

    public String drawGrid() {
        final StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int line = 0; line < 3; line++) {
                for (int x = 0; x < width; x++) {
                    if (grid[y][x] != null)
                        switch (line) {
                            case 0 -> sb
                                    .append(' ')
                                    .append(grid[y][x].getSideByDirection(SideDirection.NORTH))
                                    .append(' ');
                            case 1 -> sb
                                    .append(grid[y][x].getSideByDirection(SideDirection.WEST))
                                    .append(grid[y][x].id)
                                    .append(grid[y][x].getSideByDirection(SideDirection.EAST));
                            case 2 -> sb
                                    .append(' ')
                                    .append(grid[y][x].getSideByDirection(SideDirection.SOUTH))
                                    .append(' ');
                        }
                    else
                        sb.append("   ");

                    if (x == width - 1)
                        sb.append("\n");
                }
            }
        }
        return sb.toString();
    }
}
