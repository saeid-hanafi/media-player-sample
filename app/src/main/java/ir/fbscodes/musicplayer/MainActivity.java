package ir.fbscodes.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.slider.Slider;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ir.fbscodes.musicplayer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements MusicAdapter.onChangeMusicListener {
    private static final String TAG = "MainActivity";
    private MusicAdapter musicAdapter;
    private ActivityMainBinding binding;
    private MediaPlayer mediaPlayer;
    private MediaStatus mediaStatus;
    private Timer timer;
    private boolean isDragging;
    private int cursor;
    private List<Music> musicList;

    enum MediaStatus {
        PLAY, PAUSE, STOP
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        musicList = Music.getMusicList();
        RecyclerView recyclerView = binding.playListRv;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        musicAdapter = new MusicAdapter(musicList, this);
        recyclerView.setAdapter(musicAdapter);

        onChangeMusic(musicList.get(cursor));
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

        binding.musicSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                if (fromUser)
                    binding.startDuration.setText(Music.convertMilliSecond((long) value));
            }
        });

        binding.musicSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
                isDragging = true;
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                isDragging = false;
                mediaPlayer.seekTo((int) slider.getValue());
            }
        });

        binding.nextSiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextToMusic();
            }
        });

        binding.previousSiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousToMusic();
            }
        });
    }

    public void onChangeMusic(Music music) {
        binding.musicSlider.setValue(0);
        musicAdapter.changeMusic(music);
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
                                if (!isDragging)
                                    binding.musicSlider.setValue(mediaPlayer.getCurrentPosition());
                            }
                        });
                    }
                }, 1000, 1000);
                binding.endDuration.setText(Music.convertMilliSecond(mediaPlayer.getDuration()));
                binding.musicSlider.setValueTo(mediaPlayer.getDuration());
                mediaStatus = MediaStatus.PLAY;
                binding.playSiv.setImageResource(R.drawable.ic_baseline_pause_32);

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        nextToMusic();
                    }
                });

            }
        });

        binding.singerSdv.setActualImageResource(music.getArtistImg());
        binding.singerTv.setText(music.getArtist());
        binding.songSdv.setActualImageResource(music.getSongImg());
        binding.songTv.setText(music.getSong());
    }

    public void nextToMusic() {
        clear();
        if (cursor < musicList.size() - 1)
            cursor++;
        else
            cursor = 0;
        onChangeMusic(musicList.get(cursor));
    }

    public void previousToMusic() {
        clear();
        if (cursor > 0)
            cursor--;
        else
            cursor = musicList.size() - 1;
        onChangeMusic(musicList.get(cursor));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clear();
        mediaPlayer = null;
    }

    @Override
    public void onClick(Music music, int pos) {
        if (pos > -1 && pos != cursor) {
            cursor = pos;
            clear();
            onChangeMusic(music);
        }
    }

    private void clear() {
        timer.cancel();
        timer.purge();
        mediaPlayer.release();
    }
}