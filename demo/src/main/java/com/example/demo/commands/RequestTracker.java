package com.example.demo.commands;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.example.demo.models.RequestStatus;

// Tine evidenta cererilor asincrone, ca clientul sa le poata verifica starea
@Component
public class RequestTracker {
    private final Map<String, CompletableFuture<?>> requests = new ConcurrentHashMap<>();

    public String track(CompletableFuture<?> future) {
        String requestId = UUID.randomUUID().toString();
        requests.put(requestId, future);
        return requestId;
    }

    public Optional<RequestStatus> getStatus(String requestId) {
        CompletableFuture<?> future = requests.get(requestId);
        if (future == null) {
            return Optional.empty();
        }
        if (!future.isDone()) {
            return Optional.of(new RequestStatus(requestId, "PENDING", null, null));
        }
        if (future.isCompletedExceptionally()) {
            String error = future.exceptionNow().getMessage();
            return Optional.of(new RequestStatus(requestId, "FAILED", null, error));
        }
        return Optional.of(new RequestStatus(requestId, "DONE", future.resultNow(), null));
    }
}
