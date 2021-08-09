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
    // general
    private Client client;
    private final RlweatherPlugin plugin;
    private final RlweatherConfig config;

    // collections
    private final LinkedList<Drop> rain = new LinkedList<Drop>();
    private final LinkedList<Drop> snow = new LinkedList<Drop>();

    // misc
    private double chanceOfSpawn = 0.8;

    // garbage collectors
    private final List<Drop> spentRain = new ArrayList<Drop>();
    private final List<Drop> spentSnow = new ArrayList<Drop>();


    @Inject
    public RlweatherOverlay(Client client, RlweatherPlugin plugin, RlweatherConfig config) {
        super(plugin);
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
        this.plugin = plugin;
        this.client = client;
        this.config = config;
    }

    /**
     * Render method
     * Reads the flags set by Plugin and renders what's been asked
     *
     */
    @Override
    public Dimension render(Graphics2D graphics) {

        BufferedImage image = new BufferedImage(client.getCanvasWidth(), client.getCanvasHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics g = image.getGraphics();

        // LIGHTNING
        if(plugin.PERFORM_LIGHTNING) {
            plugin.PERFORM_LIGHTNING = false; // we only want this lasting 1fr, reset the flag early
            g.setColor(config.lightningColor());
            g.fillRect(0, 0, client.getCanvasWidth(), client.getCanvasHeight());
        }

        // RAIN
        if(plugin.PERFORM_RAIN) {
            renderDrops(rain, spentRain, image, g, "rain");
        }

        // SNOW
        if(plugin.PERFORM_SNOW) {
            renderDrops(snow, spentSnow, image, g, "snow");
        }

        // garbage collect spent rain & snow Drops
        rain.removeAll(spentRain);
        snow.removeAll(spentSnow);

        // render everything
        graphics.drawImage(image, 0, 0, null);

        return null;
    }

    /**
     * addDrop method
     * Adds a new Drop instance to the given List<Drop>
     *
     */
    private void addDrop(List list, int width, Color color, int wind, int gravity, int div) {
        list.add(new Drop(width, color, wind, gravity, div));
    }

    /**
     * renderDrops method
     * Renders Drops from a given list & stores spent ones in a garbage collection List.
     * If type "rain"; draws a line
     * If type "snow"; draws an oval
     *
     * This method exists to prevent code duplication
     *
     */
    private void renderDrops(List<Drop> drops, List<Drop> spentDrops, BufferedImage image, Graphics g, String type) {

        // drop attrs
        int thickness;
        Color color;
        int wind;
        int gravity;
        int div;

        // default to rain
        thickness = config.rainThickness();
        color = config.rainColor();
        wind = config.rainWind();
        gravity = config.rainGravity();
        div = config.rainDiv();

        // adjust if snow
        if(type.equals("snow")) {
            thickness = config.snowThickness();
            color = config.snowColor();
            wind = config.snowWind();
            gravity = config.snowGravity();
            div = config.snowDiv();
        }

        // maybe add new drop this frame
        if (Math.random() < chanceOfSpawn) {
            addDrop(drops, image.getWidth(), color, wind, gravity, div);
        }

        // loop existing Drops
        for (Drop drop : drops) {

            // queue remove spent Drops (gone offscreen height or width)
            if (drop.y1 > image.getHeight() || // offscreen down
                    drop.x1 > image.getWidth() || // offscreen right
                    drop.y1 < 0 || // offscreen up
                    drop.x1 < 0) // offscreen left
            {
                spentDrops.add(drop); // ready Drop for garbage collection
                continue;
            }

            g.setColor(drop.color);

            // if rain draw lines of thickness
            // drawLine(..) has no means of thickness so loop with offset
            if(type.equals("rain")) {
            for (int i = 0; i <= thickness; i++) {
                    g.drawLine(drop.x1 + i, drop.y1 + i, drop.x2 + i, drop.y2 + i);
                }
            }

            // if snow draw oval of thickness
            if(type.equals("snow")) {
                int radius = config.snowThickness() / 2;
                g.fillOval(drop.x1 - radius, drop.y1 - radius, thickness, thickness);
            }

            // update positions
            drop.update();
        }
    }
}
