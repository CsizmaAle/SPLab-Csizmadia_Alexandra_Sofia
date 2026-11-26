package com.example.proj.controllers;

import com.example.proj.ApiTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TreeControllerTest extends ApiTest {
    @Test void rootsAndChildren() throws Exception {
        mvc.perform(get("/tree/groups")).andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value("Lazarus Group"));
        mvc.perform(get("/tree/tactics")).andExpect(jsonPath("$[0].name").value("Execution"));
        long id = nodes.findByExternalId("G0032").orElseThrow().getId();
        mvc.perform(get("/tree/groups/" + id + "/children")).andExpect(jsonPath("$[0].name").value("WannaCry"));
        mvc.perform(get("/nodes/" + id)).andExpect(jsonPath("$.externalId").value("G0032"));
    }

    @ParameterizedTest @ValueSource(strings = {"/tree/nope", "/tree/nope/{id}/children", "/tree/groups/999999/children", "/nodes/999999"})
    void unknownViewOrNodeIs404(String url) throws Exception {
        long id = nodes.findByExternalId("G0032").orElseThrow().getId();
        mvc.perform(get(url.replace("{id}", "" + id))).andExpect(status().isNotFound());
    }
}
