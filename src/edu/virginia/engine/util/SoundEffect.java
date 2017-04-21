package edu.virginia.engine.util;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by jaz on 3/8/17.
 */
public class SoundEffect {

    public static enum Volume {
        MUTE, LOW, MEDIUM, HIGH
    }

    public static Volume volume = Volume.HIGH;

    private Clip clip;
    private int playCount = 0;

    public SoundEffect(String fileName) {
        try {
            // Use URL (instead of File) to read from disk and JAR.
            //URL url = this.getClass().getResource(fileName);
            File url = new File("resources" + File.separator + fileName);
            // Set up an audio input stream piped from the sound file.
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            // Get a clip resource.
            clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play(boolean loop) {
        if (volume != Volume.MUTE) {
            if (clip.isRunning())
                clip.stop();   // Stop the player if it is still running
            clip.setFramePosition(0); // rewind to the beginning
            clip.start();     // Start playing
            if(loop)//Loop if loop parameter is true
                clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

}

