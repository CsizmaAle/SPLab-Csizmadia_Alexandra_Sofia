package com.example.demo.services;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

// Simuleaza un serviciu extern, lent, care genereaza ISBN-uri
@Service
public class IsbnService {

    public String generateIsbn() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        long digits = ThreadLocalRandom.current().nextLong(1_000_000_000L, 10_000_000_000L);
        return "978-" + digits;
    }
}
