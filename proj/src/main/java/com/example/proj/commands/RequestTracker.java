package com.example.proj.commands;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RequestTracker {
    private final Map<String, CompletableFuture<?>> requests = new ConcurrentHashMap<>();

    public String track(CompletableFuture<?> f) {
        String id = UUID.randomUUID().toString();
        requests.put(id, f);
        return id;
    }

    public Optional<RequestStatus> status(String id) {
        return Optional.ofNullable(requests.get(id)).map(f -> switch (f.state()) {
            case RUNNING -> new RequestStatus(id, "PENDING", null, null);
            case SUCCESS -> new RequestStatus(id, "DONE", f.resultNow(), null);
            default -> new RequestStatus(id, "FAILED", null, f.exceptionNow().getMessage());
        });
    }
}
