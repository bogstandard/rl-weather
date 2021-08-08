package com.rlweather;

import java.io.*;
import java.util.Objects;
import javax.sound.sampled.*;

public class Sound {

    public static void thunder() {
        play("505113__fission9__thunder-close-2.wav");
    }

    public static void play(String soundFilePath) {
        try {
            String filePath = Objects.requireNonNull(Sound.class.getClassLoader().getResource(soundFilePath)).getPath();
            File soundFile = new File(filePath);
            AudioInputStream stream;
            AudioFormat format;
            DataLine.Info info;
            Clip clip;

            stream = AudioSystem.getAudioInputStream(soundFile);
            format = stream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(stream);
            clip.start();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
