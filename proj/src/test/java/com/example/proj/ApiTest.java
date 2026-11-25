package com.example.proj;

import com.example.proj.mitre.MitreImportService;
import com.example.proj.persistence.NodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest @Transactional
public abstract class ApiTest {
    @Autowired protected WebApplicationContext context;
    @Autowired protected NodeRepository nodes;
    @Autowired protected MitreImportService importer;
    protected MockMvc mvc;

    @BeforeEach void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        importer.importFrom(getClass().getResourceAsStream("/stix-mini.json"));
    }
}
