package com.example.proj.commands;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class AsyncCommandExecutor {
    private final ExecutorService pool = Executors.newFixedThreadPool(4);

    public <T> CompletableFuture<T> execute(Command<T> command) {
        return CompletableFuture.supplyAsync(command::execute, pool);
    }

    // nu shutdownNow: intreruperea unui thread poate inchide fisierul H2
    @PreDestroy
    void shutdown() { pool.shutdown(); }
}
