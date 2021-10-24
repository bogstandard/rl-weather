package com.rlweather;

import java.awt.Dimension;
import java.awt.Graphics2D;
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
    private final List<Drop> rain = new LinkedList<>();
    private final List<Drop> snow = new LinkedList<>();

    // misc
    private double chanceOfSpawn = 0.8;

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

        Dimension canvasDim = client.getRealDimensions();

        // LIGHTNING
        if(plugin.PERFORM_LIGHTNING) {
            plugin.PERFORM_LIGHTNING = false; // we only want this lasting 1fr, reset the flag early
            graphics.setColor(config.lightningColor());
            graphics.fillRect(0, 0, canvasDim.width, canvasDim.height);
        }

        // RAIN
        if(plugin.PERFORM_RAIN) {
            renderDrops(rain, canvasDim, graphics, "rain");
        }

        // SNOW
        if(plugin.PERFORM_SNOW) {
            renderDrops(snow, canvasDim, graphics, "snow");
        }

        return null;
    }

    /**
     * addDrop method
     * Adds a new Drop instance to the given List<Drop>
     *
     */
    private void addDrop(List<Drop> list, int width, Color color, int wind, int gravity, int div) {
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
    private void renderDrops(List<Drop> drops, Dimension c, Graphics2D g, String type) {

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
            addDrop(drops, c.width, color, wind, gravity, div);
        }

        // loop existing Drops
        for (Drop drop : drops) {
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

        // remove spent drops
        drops.removeIf(drop ->
            drop.y1 > c.height || // offscreen down
            drop.x1 > c.width || // offscreen right
            drop.y1 < 0 || // offscreen up
            drop.x1 < -c.width / 2 // offscreen left, account for spawning outside bounds in Drop.java
        );
    }
}
