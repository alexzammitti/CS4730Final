package edu.virginia.engine.util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Alex on 3/3/17.
 */
public class SoundManager {
    private static SoundManager ourInstance = new SoundManager();

    private final int BUFFER_SIZE = 128000;
    private File soundFile;
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private SourceDataLine sourceLine;
    public Clip bkgmusic;
    public Clip sound;
    public boolean loopMusic = false;

    public void setLoopMusic(boolean value) {
        this.loopMusic = value;
    }

    /**
     * @param filename the name of the file that is going to be played
     */
    public void playSound(String filename) {
        try {
            String strFilename = ("resources/" + filename);
            System.out.println(strFilename);
            soundFile = new File(strFilename);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            audioStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        audioFormat = audioStream.getFormat();

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        sourceLine.start();

        int nBytesRead = 0;
        byte[] abData = new byte[BUFFER_SIZE];
        while (nBytesRead != -1) {
            try {
                nBytesRead = audioStream.read(abData, 0, abData.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (nBytesRead >= 0) {
                @SuppressWarnings("unused")
                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
            }
        }

        sourceLine.drain();
        sourceLine.close();
    }


    public void playMusic(String filename){
        try {
            String strFilename = ("resources/" + filename);
            System.out.println(strFilename);
            soundFile = new File(strFilename);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            audioStream = AudioSystem.getAudioInputStream(soundFile);
            bkgmusic = AudioSystem.getClip();
            bkgmusic.open(audioStream);
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        audioFormat = audioStream.getFormat();

        bkgmusic.start();
    }

    public void stopMusic(Clip music) {
        if(music.isRunning()) {
            music.stop();
        }
    }

    public static SoundManager getInstance() {
        return ourInstance;
    }

    public SoundManager() {

    }
}
