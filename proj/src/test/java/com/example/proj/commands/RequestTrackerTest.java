package com.example.proj.commands;

import org.junit.jupiter.api.Test;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

class RequestTrackerTest {
    RequestTracker tracker = new RequestTracker();

    @Test void reportsPendingDoneFailedAndUnknown() {
        CompletableFuture<Integer> f = new CompletableFuture<>();
        String id = tracker.track(f);
        assertThat(tracker.status(id)).contains(new RequestStatus(id, "PENDING", null, null));
        f.complete(7);
        assertThat(tracker.status(id)).contains(new RequestStatus(id, "DONE", 7, null));
        String bad = tracker.track(CompletableFuture.failedFuture(new RuntimeException("boom")));
        assertThat(tracker.status(bad)).contains(new RequestStatus(bad, "FAILED", null, "boom"));
        assertThat(tracker.status("nope")).isEmpty();
    }
}
