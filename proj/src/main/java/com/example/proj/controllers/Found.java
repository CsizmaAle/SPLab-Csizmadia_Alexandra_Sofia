package com.example.proj.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;

final class Found {
    static <T> T found(Optional<T> o) { return o.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)); }
}
