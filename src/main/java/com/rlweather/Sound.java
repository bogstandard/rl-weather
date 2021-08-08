package com.rlweather;

import java.io.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import javax.sound.sampled.*;

public class Sound {

    private final HashMap<String, Clip> clips = new HashMap<String, Clip>();

    public void rain(String key) {
        Clip clip = play(key,"177479__unfa__slowly-raining-loop-3.wav", true);
        subscribe(key, clip);
    }

    public void thunder(String key) {
        String[] thunderSounds = {
                "195344__morninggloryproductions__thunder-2.wav",
                "352574__dobroide__20160816-thunder-03-2.wav",
                "505113__fission9__thunder-close-2.wav"
        };

        int r = new Random().nextInt(thunderSounds.length);

        Clip clip = play(key, thunderSounds[r], false);
        subscribe(key, clip);
    }

    private void subscribe(String key, Clip clip) {
        clips.put(key, clip);
    }

    public Clip play(String key, String soundFilePath, boolean loop) {
        Clip clip = null; // yuck!

        try {
            String filePath = Objects.requireNonNull(Sound.class.getClassLoader().getResource(soundFilePath)).getPath();
            File soundFile = new File(filePath);
            AudioInputStream stream;
            AudioFormat format;
            DataLine.Info info;

            stream = AudioSystem.getAudioInputStream(soundFile);
            format = stream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);

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
            System.out.println(e);
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
