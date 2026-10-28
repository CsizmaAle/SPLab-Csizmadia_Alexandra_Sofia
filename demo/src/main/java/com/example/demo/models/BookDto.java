package com.example.demo.models;

import java.util.List;

public record BookDto(Long id, String title, List<String> authors, String isbn) {
}
