package com.example.proj.controllers;

import com.example.proj.commands.AsyncCommandExecutor;
import com.example.proj.commands.RequestStatus;
import com.example.proj.commands.RequestTracker;
import com.example.proj.mitre.ImportMitreCommand;
import com.example.proj.mitre.MitreImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class ImportController {
    @Autowired private MitreImportService service;
    @Autowired private AsyncCommandExecutor executor;
    @Autowired private RequestTracker tracker;
    @Value("${mitre.url}") private String url;

    @PostMapping("/import/mitre") @ResponseStatus(HttpStatus.ACCEPTED)
    public Map<String, String> importMitre() {
        return Map.of("requestId", tracker.track(executor.execute(new ImportMitreCommand(service, url))));
    }

    @GetMapping("/requests/{id}")
    public ResponseEntity<RequestStatus> status(@PathVariable String id) {
        return ResponseEntity.of(tracker.status(id));
    }
}
