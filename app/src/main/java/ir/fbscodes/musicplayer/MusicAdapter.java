package ir.fbscodes.musicplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {
    private List<Music> musicList;
    private onChangeMusicListener onChangeMusicListener;

    public MusicAdapter(List<Music> musicList, onChangeMusicListener onChangeMusicListener) {
        this.musicList = musicList;
        this.onChangeMusicListener = onChangeMusicListener;
    }
    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MusicViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.music_items, parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        holder.bindMusic(musicList.get(position));
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView songImg;
        private TextView songText;
        private TextView artistText;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            songImg = itemView.findViewById(R.id.song_poster_playlist);
            songText = itemView.findViewById(R.id.song_playlist_tv);
            artistText = itemView.findViewById(R.id.singer_playlist_tv);
        }

        public void bindMusic(Music music) {
            songImg.setActualImageResource(music.getSongImg());
            songText.setText(music.getSong());
            artistText.setText(music.getArtist());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onChangeMusicListener.onClick(music, getAdapterPosition());
                }
            });
        }
    }

    public void changeMusic(Music music) {
        int index = musicList.indexOf(music);
        if (index > -1) {

        }
    }

    public interface onChangeMusicListener {
        void onClick(Music music, int pos);
    }
}
