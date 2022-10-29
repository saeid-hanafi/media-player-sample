package ir.fbscodes.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ir.fbscodes.musicplayer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private MediaPlayer mediaPlayer;
    private MediaStatus mediaStatus;
    private Timer timer;

    enum MediaStatus {
        PLAY, PAUSE, STOP
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        List<Music> musicList = Music.getMusicList();
        RecyclerView recyclerView = binding.playListRv;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(new MusicAdapter(musicList));

        onChangeMusic(musicList.get(0));
        binding.playSiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mediaStatus) {
                    case PLAY:
                        mediaPlayer.pause();
                        mediaStatus = MediaStatus.PAUSE;
                        binding.playSiv.setImageResource(R.drawable.ic_baseline_play_arrow_32);
                        break;
                    case STOP:
                    case PAUSE:
                        mediaPlayer.start();
                        mediaStatus = MediaStatus.PLAY;
                        binding.playSiv.setImageResource(R.drawable.ic_baseline_pause_32);
                        break;
                }
            }
        });
    }

    public void onChangeMusic(Music music) {
        mediaPlayer = MediaPlayer.create(this, music.getMusic());
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.startDuration.setText(Music.convertMilliSecond(mediaPlayer.getCurrentPosition()));
                                binding.musicSlider.setValue(mediaPlayer.getCurrentPosition());
                            }
                        });
                    }
                }, 1000, 1000);
                binding.endDuration.setText(Music.convertMilliSecond(mediaPlayer.getDuration()));
                binding.musicSlider.setValueTo(mediaPlayer.getDuration());
                mediaStatus = MediaStatus.PLAY;
                binding.playSiv.setImageResource(R.drawable.ic_baseline_pause_32);

            }
        });

        binding.singerSdv.setActualImageResource(music.getArtistImg());
        binding.singerTv.setText(music.getArtist());
        binding.songSdv.setActualImageResource(music.getSongImg());
        binding.songTv.setText(music.getSong());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}