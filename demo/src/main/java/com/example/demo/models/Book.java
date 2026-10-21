package com.example.demo.models;

import java.util.List;

public record Book(Long id, String title, List<String> authors, String isbn) {
}
