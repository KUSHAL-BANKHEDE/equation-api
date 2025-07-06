package com.example.demo.service;

import com.example.demo.model.Equation;
import com.example.demo.model.EquationNode;
import com.example.demo.repository.EquationRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EquationService {
    private final EquationRepository repository = new EquationRepository();

    private static final Set<String> OPERATORS = Set.of("+", "-", "*", "/", "^", "(", ")");
    private static final Map<String, Integer> PRECEDENCE = Map.of(
            "+", 1,
            "-", 1,
            "*", 2,
            "/", 2,
            "^", 3
    );

    public String storeEquation(String infix) {
        List<String> postfix = infixToPostfix(tokenize(infix));
        EquationNode root = buildTree(postfix);
        Equation eq = new Equation(null, infix, root);
        return repository.save(infix, eq);
    }

    public List<Equation> getAllEquations() {
        return repository.getAll();
    }

    public Equation getEquationById(String id) {
        return repository.getById(id);
    }

    public double evaluate(String id, Map<String, Double> variables) {
        Equation eq = repository.getById(id);
        if (eq == null) throw new IllegalArgumentException("Equation not found");
        return evaluateNode(eq.getRoot(), variables);
    }

    public String reconstructInfix(EquationNode node) {
        if (node == null) return "";
        if (node.getLeft() == null && node.getRight() == null) return node.getValue();
        String left = reconstructInfix(node.getLeft());
        String right = reconstructInfix(node.getRight());
        return "(" + left + " " + node.getValue() + " " + right + ")";
    }

    // --- Helper methods ---
    private List<String> tokenize(String expr) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (char c : expr.replaceAll("\\s+", "").toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                sb.append(c);
            } else {
                if (sb.length() > 0) {
                    tokens.add(sb.toString());
                    sb.setLength(0);
                }
                tokens.add(String.valueOf(c));
            }
        }
        if (sb.length() > 0) tokens.add(sb.toString());
        return tokens;
    }

    private List<String> infixToPostfix(List<String> tokens) {
        List<String> output = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();
        for (String token : tokens) {
            if (!OPERATORS.contains(token)) {
                output.add(token);
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    output.add(stack.pop());
                }
                stack.pop(); // Remove '('
            } else {
                while (!stack.isEmpty() && !stack.peek().equals("(") &&
                        PRECEDENCE.getOrDefault(token, 0) <= PRECEDENCE.getOrDefault(stack.peek(), 0)) {
                    output.add(stack.pop());
                }
                stack.push(token);
            }
        }
        while (!stack.isEmpty()) output.add(stack.pop());
        return output;
    }

    private EquationNode buildTree(List<String> postfix) {
        Deque<EquationNode> stack = new ArrayDeque<>();
        for (String token : postfix) {
            if (!OPERATORS.contains(token)) {
                stack.push(new EquationNode(token));
            } else {
                EquationNode right = stack.pop();
                EquationNode left = stack.pop();
                stack.push(new EquationNode(token, left, right));
            }
        }
        return stack.pop();
    }

    private double evaluateNode(EquationNode node, Map<String, Double> variables) {
        if (node.getLeft() == null && node.getRight() == null) {
            String val = node.getValue();
            if (val.matches("[a-zA-Z]+")) {
                if (!variables.containsKey(val)) throw new IllegalArgumentException("Missing variable: " + val);
                return variables.get(val);
            } else {
                return Double.parseDouble(val.replaceAll("[^0-9.]", ""));
            }
        }
        double left = evaluateNode(node.getLeft(), variables);
        double right = evaluateNode(node.getRight(), variables);
        return switch (node.getValue()) {
            case "+" -> left + right;
            case "-" -> left - right;
            case "*" -> left * right;
            case "/" -> left / right;
            case "^" -> Math.pow(left, right);
            default -> throw new IllegalArgumentException("Unknown operator: " + node.getValue());
        };
    }
} 