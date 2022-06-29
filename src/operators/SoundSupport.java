package operators;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class SoundSupport {

    private Clip background;
    private long clipTime = 0;

    public SoundSupport() {
        background = getClip(loadAudio("background"));
    }

    private AudioInputStream loadAudio(String url) {
        try {
            InputStream audioSrc = getClass().getResourceAsStream("/media/audio/" + url + ".wav");
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            return AudioSystem.getAudioInputStream(bufferedIn);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    private Clip getClip(AudioInputStream stream) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(stream);
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void resumeBackground(){
        background.setMicrosecondPosition(clipTime);
        background.start();
    }

    public void pauseBackground(){
        clipTime = background.getMicrosecondPosition();
        background.stop();
    }

    public void restartBackground() {
        clipTime = 0;
        resumeBackground();
    }

    public void playJump() {
        Clip clip = getClip(loadAudio("jump"));
        clip.start();

    }

    public void playDiamond() {
        Clip clip = getClip(loadAudio("diamond"));
        clip.start();

    }

    public void playArrow() {
        Clip clip = getClip(loadAudio("arrow"));
        clip.start();

    }

    public void playGameOver() {
        Clip clip = getClip(loadAudio("gameOver"));
        clip.start();

    }

    public void playStomp() {
        Clip clip = getClip(loadAudio("stomp"));
        clip.start();

    }

    public void playOneUp() {
        Clip clip = getClip(loadAudio("oneUp"));
        clip.start();

    }

    public void playSuperMushroom() {

        Clip clip = getClip(loadAudio("superMushroom"));
        clip.start();

    }

    public void playSteveDies() {

        Clip clip = getClip(loadAudio("steveDies"));
        clip.start();

    }

    public void playRose() {
        Clip clip = getClip(loadAudio("diamond"));
        clip.start();
    }
}
