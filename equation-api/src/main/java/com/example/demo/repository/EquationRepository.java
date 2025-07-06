package com.example.demo.repository;

import com.example.demo.model.Equation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EquationRepository {
    private final Map<String, Equation> equationMap = new ConcurrentHashMap<>();
    private int idCounter = 1;

    public synchronized String save(String infix, Equation equation) {
        String id = String.valueOf(idCounter++);
        Equation eq = new Equation(id, infix, equation.getRoot());
        equationMap.put(id, eq);
        return id;
    }

    public Equation getById(String id) {
        return equationMap.get(id);
    }

    public List<Equation> getAll() {
        return new ArrayList<>(equationMap.values());
    }
} 