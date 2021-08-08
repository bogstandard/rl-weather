package com.rlweather;

import java.io.*;
import java.util.Objects;
import java.util.Random;
import javax.sound.sampled.*;

public class Sound {

    public static Clip rain() {
        return play("177479__unfa__slowly-raining-loop-2.wav", true);
    }

    public static void thunder() {

        String[] thunderSounds = {
                "195344__morninggloryproductions__thunder-2.wav",
                "352574__dobroide__20160816-thunder-03-2.wav",
                "505113__fission9__thunder-close-2.wav"
        };

        int r = new Random().nextInt(thunderSounds.length);

        play(thunderSounds[r], false);
    }

    public static Clip play(String soundFilePath, boolean loop) {
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
}
