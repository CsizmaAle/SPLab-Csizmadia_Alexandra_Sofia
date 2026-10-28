package com.example.demo.models;

import java.awt.Dimension;

public interface Picture {
    String url();

    Dimension dim();

    PictureContent content();
}
