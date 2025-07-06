package com.example.demo.controller;

import com.example.demo.model.Equation;
import com.example.demo.service.EquationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/equations")
public class EquationController {
    private final EquationService equationService;

    public EquationController(EquationService equationService) {
        this.equationService = equationService;
    }

    @PostMapping("/store")
    public ResponseEntity<Map<String, Object>> storeEquation(@RequestBody Map<String, String> request) {
        String infix = request.get("equation");
        String id = equationService.storeEquation(infix);
        Map<String, Object> resp = new HashMap<>();
        resp.put("message", "Equation stored successfully");
        resp.put("equationId", id);
        return ResponseEntity.ok(resp);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllEquations() {
        List<Equation> equations = equationService.getAllEquations();
        List<Map<String, String>> eqList = equations.stream().map(eq -> {
            Map<String, String> m = new HashMap<>();
            m.put("equationId", eq.getEquationId());
            m.put("equation", eq.getInfix());
            return m;
        }).toList();
        Map<String, Object> resp = new HashMap<>();
        resp.put("equations", eqList);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/{equationId}/evaluate")
    public ResponseEntity<Map<String, Object>> evaluateEquation(
            @PathVariable String equationId,
            @RequestBody Map<String, Map<String, Double>> request) {
        try {
            Map<String, Double> variables = request.get("variables");
            Equation eq = equationService.getEquationById(equationId);
            double result = equationService.evaluate(equationId, variables);
            Map<String, Object> resp = new HashMap<>();
            resp.put("equationId", eq.getEquationId());
            resp.put("equation", eq.getInfix());
            resp.put("variables", variables);
            resp.put("result", result);
            return ResponseEntity.ok(resp);
        } catch (IllegalArgumentException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
} 