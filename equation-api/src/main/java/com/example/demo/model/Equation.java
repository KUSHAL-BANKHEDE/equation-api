package com.example.demo.model;

public class Equation {
    private String equationId;
    private String infix;
    private EquationNode root;

    public Equation(String equationId, String infix, EquationNode root) {
        this.equationId = equationId;
        this.infix = infix;
        this.root = root;
    }

    public String getEquationId() {
        return equationId;
    }

    public String getInfix() {
        return infix;
    }

    public EquationNode getRoot() {
        return root;
    }
} 