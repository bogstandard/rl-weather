/**
 * Drop class
 *
 * Basic information for a droplet of rain or snow, update()
 * causes the droplet to fall a little farther.
 *
 */

package com.rlweather;

import java.awt.Color;
import java.util.Random;

public class Drop {
    int x1;
    int y1;
    int x2;
    int y2;
    Color color;
    int wind;
    int gravity;
    int div;

    public Drop(int width, Color color, int wind, int gravity, int div) {
        Random r = new Random();
        this.x1 = r.nextInt(width + (width/2)) - (width/2); // -width/2 to width, java lol
        this.y1 = 0;
        this.x2 = x1;
        this.y2 = y1;
        this.color = color;
        this.wind = wind;
        this.gravity = gravity;
        this.div = div;
    }

    public void update() {
        Random r = new Random();

        this.x2 = this.x1;
        this.y2 = this.y1;

        this.x1 += this.wind + r.nextInt(this.div + this.div) - this.div; // -div to div
        this.y1 += this.gravity;
    }
}
