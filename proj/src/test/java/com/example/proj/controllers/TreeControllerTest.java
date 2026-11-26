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

    @Test void summaryEndpoint() throws Exception {
        long id = nodes.findByExternalId("G0032").orElseThrow().getId();
        mvc.perform(get("/nodes/" + id + "/summary")).andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Lazarus Group")).andExpect(jsonPath("$.nodeId").value(id))
            .andExpect(jsonPath("$.counts.SOFTWARE").value(1)).andExpect(jsonPath("$.counts.TECHNIQUE").value(1))
            .andExpect(jsonPath("$.impactTotal").value(0.0));
        long execution = nodes.findByExternalId("TA0002").orElseThrow().getId();
        mvc.perform(get("/nodes/" + execution + "/summary?view=tactics")).andExpect(jsonPath("$.counts.TECHNIQUE").value(2));
        mvc.perform(get("/nodes/" + id + "/summary?view=nope")).andExpect(status().isNotFound());
        mvc.perform(get("/nodes/999999/summary")).andExpect(status().isNotFound());
    }
}
