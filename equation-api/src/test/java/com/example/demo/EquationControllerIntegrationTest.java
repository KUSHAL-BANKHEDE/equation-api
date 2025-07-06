package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EquationControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testStoreRetrieveAndEvaluateEquation() throws Exception {
        // Store equation
        var storeResp = mockMvc.perform(post("/api/equations/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"equation\":\"3*x + 2*y - z\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.equationId").exists())
                .andReturn();
        String equationId = objectMapper.readTree(storeResp.getResponse().getContentAsString()).get("equationId").asText();

        // Retrieve equations
        mockMvc.perform(get("/api/equations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.equations[0].equationId").value(equationId));

        // Evaluate equation
        String evalReq = objectMapper.writeValueAsString(Map.of("variables", Map.of("x", 2, "y", 3, "z", 1)));
        mockMvc.perform(post("/api/equations/" + equationId + "/evaluate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(evalReq))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(10.0));
    }

    @Test
    void testEvaluateWithMissingVariable() throws Exception {
        var storeResp = mockMvc.perform(post("/api/equations/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"equation\":\"x + y\"}"))
                .andReturn();
        String equationId = objectMapper.readTree(storeResp.getResponse().getContentAsString()).get("equationId").asText();
        String evalReq = objectMapper.writeValueAsString(Map.of("variables", Map.of("x", 2)));
        mockMvc.perform(post("/api/equations/" + equationId + "/evaluate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(evalReq))
                .andExpect(status().is4xxClientError());
    }
} 