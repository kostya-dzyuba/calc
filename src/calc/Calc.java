package calc;

import java.util.ArrayList;

public class Calc {

    public static int calc(String expr) {
        ArrayList<Object> tokens = tokenize(expr);
        desugar(tokens);
        applyParens(tokens);
        return calc(tokens);
    }

    private static ArrayList<Object> tokenize(String expr) {
        ArrayList<Object> tokens = new ArrayList<>();
        char[] chars = expr.toCharArray();
        Operator op;
        for (int i = 0; i < expr.length(); i++) {
            if (Character.isDigit(chars[i])) {
                int start = i;
                while (i < chars.length && Character.isDigit(chars[i])) i++;
                tokens.add(Integer.parseInt(expr.substring(start, i)));
                i--;
            } else if ((op = Operator.create(chars[i])) != null) {
                tokens.add(op);
            } else if (chars[i] == '(' || chars[i] == ')') {
                tokens.add(chars[i]);
            } else throw new IllegalArgumentException(String.valueOf(chars[i]));
        }
        return tokens;
    }

    private static void desugar(ArrayList<Object> tokens) {
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i) instanceof Character c) {
                if (c == '(' && i > 0 && tokens.get(i - 1) instanceof Integer) {
                    tokens.add(i, Operator.create('*'));
                } else if (c == ')' && i + 1 < tokens.size() && tokens.get(i + 1) instanceof Integer) {
                    tokens.add(i + 1, Operator.create('*'));
                }
            } else if (tokens.get(i) instanceof Operator op) {
                if (op.represent == '√' && i == 0 || op.represent == '√' && !(tokens.get(i - 1) instanceof Integer)) {
                    tokens.add(i, 1);
                }
            }
        }
    }

    private static void applyParens(ArrayList<Object> tokens) {
        if (!checkParens(tokens)) return;
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i) instanceof Character left && left == '(') {
                int leftIndex = i;
                for (; !(tokens.get(i + 2) instanceof Character c && c == ')'); i += 2) {
                    Operator op = (Operator) tokens.get(i + 2);
                    op.priority = 2;
                }
                tokens.remove(i + 2);
                tokens.remove(leftIndex);
            }
        }
    }

    private static boolean checkParens(ArrayList<Object> tokens) {
        int parentheses = 0;
        for (Object token : tokens) {
            if (token instanceof Character c)
                if (c == '(') parentheses++;
                else if (c == ')') parentheses--;
        }
        return parentheses == 0;
    }

    private static int calc(ArrayList<Object> tokens) {
        while (tokens.size() > 1) {
            int maxPriority = getMaxPriority(tokens);
            for (int i = 0; i < tokens.size(); i++) {
                Object token = tokens.get(i);
                if (token instanceof Operator operator && operator.priority == maxPriority) {
                    int a = (int) tokens.get(i - 1);
                    int b = (int) tokens.get(i + 1);
                    int result = operator.operation.applyAsInt(a, b);
                    tokens.set(i, result);
                    tokens.remove(i + 1);
                    tokens.remove(i - 1);
                }
            }
        }
        return ((int) tokens.get(0));
    }

    private static int getMaxPriority(ArrayList<Object> tokens) {
        int priority = 0;
        for (Object token : tokens)
            if (token instanceof Operator operator)
                priority = Math.max(priority, operator.priority);
        return priority;
    }
}