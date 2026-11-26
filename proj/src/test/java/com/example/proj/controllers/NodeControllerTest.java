package com.example.proj.controllers;

import com.example.proj.ApiTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NodeControllerTest extends ApiTest {
    String json(String type, String name) { return "{\"type\":\"%s\",\"name\":\"%s\",\"value\":4000000000}".formatted(type, name); }
    long lazarus() { return nodes.findByExternalId("G0032").orElseThrow().getId(); }

    @Test void attachesChildAndShowsItInTree() throws Exception {
        mvc.perform(post("/nodes/" + lazarus() + "/children").contentType(APPLICATION_JSON).content(json("IMPACT", " Pagube WannaCry ")))
           .andExpect(status().isCreated()).andExpect(jsonPath("$.source").value("CUSTOM"))
           .andExpect(jsonPath("$.name").value("Pagube WannaCry")).andExpect(jsonPath("$.value").value(4.0E9));
        mvc.perform(get("/tree/groups/" + lazarus() + "/children")).andExpect(jsonPath("$[*].name", hasItem("Pagube WannaCry")));
    }

    @Test void rejectsBlankNameAndMissingParent() throws Exception {
        mvc.perform(post("/nodes/" + lazarus() + "/children").contentType(APPLICATION_JSON).content(json("NOTE", " "))).andExpect(status().isBadRequest());
        mvc.perform(post("/nodes/999999/children").contentType(APPLICATION_JSON).content(json("NOTE", "x"))).andExpect(status().isNotFound());
    }

    @ParameterizedTest @ValueSource(strings = {
        "{\"name\":\"fara tip\"}", "{\"type\":\"NOTE\"}", "{\"type\":\"NOPE\",\"name\":\"x\"}",
        "{\"type\":\"NOTE\",\"name\":\"N256\"}", "{\"type\":\"NOTE\",\"name\":\"x\",\"url\":\"U256\"}", "{\"type\":\"NOTE\",\"name\":\"x\",\"text\":\"T20001\"}"})
    void rejectsInvalidBody(String body) throws Exception {
        body = body.replace("N256", "n".repeat(256)).replace("U256", "u".repeat(256)).replace("T20001", "t".repeat(20_001));
        mvc.perform(post("/nodes/" + lazarus() + "/children").contentType(APPLICATION_JSON).content(body)).andExpect(status().isBadRequest());
    }

    @Test void acceptsMaxLengths() throws Exception {
        String body = "{\"type\":\"NOTE\",\"name\":\"%s\",\"url\":\"%s\",\"text\":\"%s\"}".formatted("n".repeat(255), "u".repeat(255), "t".repeat(20_000));
        mvc.perform(post("/nodes/" + lazarus() + "/children").contentType(APPLICATION_JSON).content(body)).andExpect(status().isCreated());
    }
}
