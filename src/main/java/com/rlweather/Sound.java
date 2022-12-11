package com.rlweather;

import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import javax.sound.sampled.*;

@Slf4j
public class Sound {

    private final HashMap<String, Clip> clips = new HashMap<String, Clip>();

    public void rain(String key) {
        Clip clip = play(key,"/normalized/177479__unfa__slowly-raining-loop-3.wav", true);
        subscribe(key, clip);
    }

    public void thunder(String key) {
        String[] thunderSounds = {
                "/normalized/195344__morninggloryproductions__thunder-2.wav",
                "/normalized/352574__dobroide__20160816-thunder-03-2.wav",
                "/normalized/505113__fission9__thunder-close-2.wav"
        };

        int r = new Random().nextInt(thunderSounds.length);

        Clip clip = play(key, thunderSounds[r], false);
        subscribe(key, clip);
    }
    
    public void snow(String key) {
        Clip clip = play(key,"/normalized/201208__rivv3t__raw-wind_edited.wav", true);
        subscribe(key, clip);
    }

    private void subscribe(String key, Clip clip) {
        clips.put(key, clip);
    }

    public Clip play(String key, String soundFilePath, boolean loop) {
        Clip clip = null; // yuck!

        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(getClass().getResource(soundFilePath));
            clip = AudioSystem.getClip();

            // callback once sound is completed
            // removes clip from clips map
            clip.addLineListener(e -> {
                if (e.getType() == LineEvent.Type.STOP) {
                    clips.remove(key);
                }
            });

            clip.open(stream);
            clip.start();

            if(loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }

            return clip;
        }
        catch (Exception e) {
            log.warn("Failed to play sound: " + soundFilePath, e);
        }

        return clip;
    }

    public boolean isPlaying(String key) {
        return clips.containsKey(key);
    }

    public void stop(String key) {
        Clip clip = clips.get(key);
        try {
            clip.stop();
        } catch (Exception e) {
            // bzzt, likely no clip by that key
        }
    }

    public void stopAll() {
        for (String key : clips.keySet()) {
            stop(key);
        }
    }
}
