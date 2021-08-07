package com.rlweather;

import java.awt.Color;
import java.util.Random;

public class Rain {
    int x1;
    int y1;
    int x2;
    int y2;
    Color color;
    float wind;
    float gravity;

    public Rain(int width, Color color, float wind, float gravity) {
        Random r = new Random();
        this.x1 = r.nextInt(width);
        this.y1 = 0;
        this.x2 = x1;
        this.y2 = y1;
        this.color = color;
        this.wind = wind;
        this.gravity = gravity;
    }

    public void update() {
        this.x2 = this.x1;
        this.y2 = this.y1;

        this.x1 += this.wind;
        this.y1 += this.gravity;
    }
}
