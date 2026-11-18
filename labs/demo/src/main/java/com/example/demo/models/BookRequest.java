package com.example.demo.models;

import java.util.List;

// Corpul cererilor POST / PUT pentru /books
public record BookRequest(String title, List<String> authors) {
}
