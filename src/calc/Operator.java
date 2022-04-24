package calc;

import java.util.function.IntBinaryOperator;

public class Operator {

    public final char represent;
    public final IntBinaryOperator operation;
    public int priority;

    private Operator(char represent,
                     IntBinaryOperator operation,
                     int priority) {
        this.represent = represent;
        this.operation = operation;
        this.priority = priority;
    }

    public static Operator create(char c) {
        return switch (c) {
            case '+' -> new Operator(c, Integer::sum, 0);
            case '-' -> new Operator(c, (a, b) -> a - b, 0);
            case '*' -> new Operator(c, (a, b) -> a * b, 1);
            case '/' -> new Operator(c, (a, b) -> a / b, 1);
            case '^' -> new Operator(c, (a, b) -> (int) Math.pow(a, b), 2);
            case '√' -> new Operator(c, (a, b) -> a * (int) Math.sqrt(b), 2);
            default -> null;
        };
    }

    @Override
    public String toString() {
        return String.valueOf(represent) + priority;
    }
}