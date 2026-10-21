package com.example.demo.commands;

import java.util.concurrent.CompletableFuture;

public interface CommandExecutor {
    <T> CompletableFuture<T> execute(Command<T> command);
}
