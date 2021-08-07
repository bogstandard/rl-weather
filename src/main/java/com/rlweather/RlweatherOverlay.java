package com.rlweather;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import java.awt.Color;

public class RlweatherOverlay extends Overlay
{
    private Client client;
    private RlweatherConfig config;

    private final LinkedList<Rain> rain = new LinkedList<Rain>();

    private float wind = 2.05f;
    private float gravity = 9.8f;


    @Inject
    public RlweatherOverlay(Client client, RlweatherPlugin plugin, RlweatherConfig config) {
        super(plugin);
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
        this.client = client;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics) {

        BufferedImage image = new BufferedImage(client.getCanvasWidth(), client.getCanvasHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics g = image.getGraphics();

        // RAIN

        // add new rain every tick
        addRain(image.getWidth(), config.rainColor(), wind, gravity);

        // loop existing rain
        List<Rain> spentRain = new ArrayList<Rain>(); // garbage collection
        for (Rain rainStreak : rain) {

            // queue remove spent rain (gone offscreen height or width)
            if (rainStreak.y1 >= image.getHeight() || rainStreak.x1 >= image.getWidth()) {
                spentRain.add(rainStreak);
                continue;
            }

            g.setColor(rainStreak.color);

            for (int i=0; i<=config.rainThickness(); i++) {
                g.drawLine(rainStreak.x1 + i, rainStreak.y1 + i, rainStreak.x2 + i, rainStreak.y2 + i);
            }

            // update positions
            rainStreak.update();
        }

        // remove spent rain
        rain.removeAll(spentRain);

        // render everything
        graphics.drawImage(image, 0, 0, null);

        return null;
    }

    private void addRain(int width, Color color, float wind, float gravity) {
        rain.add(new Rain(width, color, wind, gravity));
    }
}
