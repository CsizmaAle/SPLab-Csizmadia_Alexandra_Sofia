package com.example.demo.models;

// Corpul cererii POST /books/{id}/elements
// type: Section, Paragraph, Image, Table sau TableOfContents
// text: titlul (Section, Table, TableOfContents), textul (Paragraph) sau url-ul (Image)
// parentId: id-ul sectiunii in care se adauga; null = direct in carte
public record ElementRequest(String type, String text, Integer parentId) {
}
