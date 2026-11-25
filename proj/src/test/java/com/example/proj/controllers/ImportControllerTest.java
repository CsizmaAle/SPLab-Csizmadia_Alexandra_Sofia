package com.example.proj.controllers;

import com.example.proj.ApiTest;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;

import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ImportControllerTest extends ApiTest {
    @Test void startsImportAndReportsStatus() throws Exception {
        String body = mvc.perform(post("/import/mitre")).andExpect(status().isAccepted())
            .andReturn().getResponse().getContentAsString();
        String id = JsonPath.read(body, "$.requestId");
        // mitre.url din teste nu exista, deci importul trebuie sa esueze
        await().untilAsserted(() -> mvc.perform(get("/requests/" + id)).andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id)).andExpect(jsonPath("$.status").value("FAILED")));
        mvc.perform(get("/requests/nope")).andExpect(status().isNotFound());
    }
}
