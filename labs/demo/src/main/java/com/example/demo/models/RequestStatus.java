package com.example.demo.models;

public record RequestStatus(String requestId, String status, Object result, String error) {
}
