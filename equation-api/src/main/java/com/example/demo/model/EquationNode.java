package com.example.demo.model;

public class EquationNode {
    private String value;
    private EquationNode left;
    private EquationNode right;

    public EquationNode(String value) {
        this.value = value;
    }

    public EquationNode(String value, EquationNode left, EquationNode right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    public String getValue() {
        return value;
    }

    public EquationNode getLeft() {
        return left;
    }

    public EquationNode getRight() {
        return right;
    }

    public void setLeft(EquationNode left) {
        this.left = left;
    }

    public void setRight(EquationNode right) {
        this.right = right;
    }
} 