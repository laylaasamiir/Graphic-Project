package CarGame;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sound {




        private MediaPlayer player;

        public Sound(String fileName) {

            new JFXPanel();

            try {
                Media media = new Media(getClass().getResource("/Sounds/" + fileName).toExternalForm());
                player = new MediaPlayer(media);
            } catch (Exception e) {
                System.out.println("Error loading sound: " + fileName);
                e.printStackTrace();
            }
        }

        public void play() {
            if (player != null) {
                player.stop();
                player.play();
            }
        }

        public void loop() {
            if (player != null) {
                player.setCycleCount(MediaPlayer.INDEFINITE);
                player.play();
            }
        }

        public void stop() {
            if (player != null) {
                player.stop();
            }
        }

        public void setVolume(double v) {
            if (player != null) {
                player.setVolume(v);
            }
        }

        public void pause() {
            if (player != null) {
                player.pause();
            }
        }

}
