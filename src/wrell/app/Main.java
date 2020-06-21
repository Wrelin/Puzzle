package wrell.app;

import wrell.app.utility.Puzzle;

public class Main {

    public static void main(String[] args) {
        var puzzle = Puzzle.getByArray(new String[]{
                "()((", ")(((",
                "|()(", "))|(", "())|", "|))(", "(|))", ")|)(",
                "))||", "||((", "||((", "||))",
        });
        if (puzzle.check()) {
            puzzle.getVariants().forEach(System.out::println);
        }
    }
}
