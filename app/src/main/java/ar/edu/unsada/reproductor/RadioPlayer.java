package ar.edu.unsada.reproductor;

    import android.media.MediaPlayer;

public class RadioPlayer {
    private MediaPlayer mediaPlayer;

    public void playRadio(String streamUrl) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(streamUrl);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pauseRadio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void stopRadio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}