package com.example.demo;

import com.example.demo.service.EquationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Map;

class EquationApiApplicationTests {

	@Test
	void contextLoads() {
		// Context load test
	}

	@Test
	void testStoreRetrieveEvaluateEquation() {
		EquationService service = new EquationService();
		String id = service.storeEquation("3*x + 2*y - z");
		Assertions.assertNotNull(id);
		var eq = service.getEquationById(id);
		Assertions.assertEquals("3*x + 2*y - z", eq.getInfix());
		double result = service.evaluate(id, Map.of("x", 2.0, "y", 3.0, "z", 1.0));
		Assertions.assertEquals(10.0, result, 0.0001);
	}
}
