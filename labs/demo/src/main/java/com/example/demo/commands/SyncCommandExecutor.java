package com.example.demo.commands;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

// Ruleaza comanda pe thread-ul curent; rezultatul e gata imediat
@Component
public class SyncCommandExecutor implements CommandExecutor {

    @Override
    public <T> CompletableFuture<T> execute(Command<T> command) {
        return CompletableFuture.completedFuture(command.execute());
    }
}
