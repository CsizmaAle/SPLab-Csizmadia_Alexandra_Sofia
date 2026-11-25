package com.example.proj.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AsyncCommandExecutorTest {
    AsyncCommandExecutor executor = new AsyncCommandExecutor();

    @AfterEach void stop() { executor.shutdown(); }

    @Test void runsCommandOnAnotherThread() {
        assertThat(executor.execute(Thread::currentThread).join()).isNotSameAs(Thread.currentThread());
    }

    @Test void failedCommandKeepsItsMessage() {
        RequestTracker tracker = new RequestTracker();
        CompletableFuture<Object> f = executor.execute(() -> { throw new IllegalStateException("boom"); });
        assertThatThrownBy(f::join).hasRootCauseMessage("boom");
        assertThat(tracker.status(tracker.track(f))).get().extracting(RequestStatus::error).isEqualTo("boom");
    }

    @Test void rejectsCommandsAfterShutdown() {
        executor.shutdown();
        assertThatThrownBy(() -> executor.execute(() -> 1)).isInstanceOf(RejectedExecutionException.class);
    }
}
