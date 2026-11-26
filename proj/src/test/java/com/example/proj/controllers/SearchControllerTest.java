package com.example.proj.controllers;

import com.example.proj.ApiTest;
import com.example.proj.model.Node;
import com.example.proj.model.NodeType;
import com.example.proj.model.Source;
import org.junit.jupiter.api.Test;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SearchControllerTest extends ApiTest {
    @Test void findsInNameAndText() throws Exception {
        mvc.perform(get("/search").param("q", "wannacry")).andExpect(jsonPath("$[*].name", hasItem("WannaCry")));
        mvc.perform(get("/search").param("q", "ENCRYPT")).andExpect(jsonPath("$[*].externalId", hasItem("T1486")));
        mvc.perform(get("/search").param("q", "  wannacry  ")).andExpect(jsonPath("$[*].name", hasItem("WannaCry")));
    }

    @Test void filtersByType() throws Exception {
        mvc.perform(get("/search").param("q", "a").param("type", "TACTIC"))
            .andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[*].type", everyItem(is("TACTIC"))));
    }

    @Test void returnsAtMost50() throws Exception {
        IntStream.range(0, 51).forEach(i -> nodes.save(new Node(NodeType.NOTE, "zz" + i, Source.CUSTOM)));
        mvc.perform(get("/search").param("q", "zz")).andExpect(jsonPath("$", hasSize(50)));
    }

    @Test void blankQueryIs400() throws Exception {
        mvc.perform(get("/search").param("q", "   ")).andExpect(status().isBadRequest());
        mvc.perform(get("/search")).andExpect(status().isBadRequest());
        mvc.perform(get("/search").param("q", "a").param("type", "NOPE")).andExpect(status().isBadRequest());
    }
}
