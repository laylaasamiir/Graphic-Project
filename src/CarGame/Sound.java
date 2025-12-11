package CarGame;

import javax.sound.sampled.*;
import java.io.File;

public class Sound {

    private Clip clip;

    public Sound(String fileName) {
        try {
            File file = new File("src/CarGame/" + fileName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (Exception e) {
            System.out.println("Error loading sound: " + fileName);
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip == null) return;
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    public void loop() {
        if (clip == null) return;
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        if (clip != null) clip.stop();
    }

    public void setVolume(float volume) {
        if (clip == null) return;

        FloatControl gainControl =
                (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        float dB = (float) (Math.log(volume) / Math.log(10) * 20);
        gainControl.setValue(dB);
    }
}
