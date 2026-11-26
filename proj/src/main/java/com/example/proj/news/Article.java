package com.example.proj.news;

import java.time.Instant;

public record Article(String title, String summary, String url, Instant publishedAt, String body) {}
