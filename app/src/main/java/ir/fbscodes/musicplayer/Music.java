package ir.fbscodes.musicplayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Music {
    private int id;
    private String artist;
    private String song;
    private int artistImg;
    private int songImg;
    private int music;

    public int getMusic() {
        return music;
    }

    public void setMusic(int music) {
        this.music = music;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public int getArtistImg() {
        return artistImg;
    }

    public void setArtistImg(int artistImg) {
        this.artistImg = artistImg;
    }

    public int getSongImg() {
        return songImg;
    }

    public void setSongImg(int songImg) {
        this.songImg = songImg;
    }

    public static List<Music> getMusicList() {
        List<Music> list = new ArrayList<>();

        Music music1 = new Music();
        music1.setId(1);
        music1.setArtist("Justin Bieber");
        music1.setSong("First Song");
        music1.setArtistImg(R.drawable.bieber);
        music1.setSongImg(R.drawable.justin_bieber_pic);
        music1.setMusic(R.raw.justin_bieber1);
        list.add(music1);

        Music music2 = new Music();
        music2.setId(2);
        music2.setArtist("Justin Bieber");
        music2.setSong("Second Song");
        music2.setArtistImg(R.drawable.justin);
        music2.setSongImg(R.drawable.justin_bieber_pic1);
        music2.setMusic(R.raw.justin_bieber2);
        list.add(music2);

        Music music3 = new Music();
        music3.setId(3);
        music3.setArtist("Justin Bieber");
        music3.setSong("Third Song");
        music3.setArtistImg(R.drawable.bieber);
        music3.setSongImg(R.drawable.justin_bieber_pic2);
        music3.setMusic(R.raw.justin_bieber3);
        list.add(music3);

        Music music4 = new Music();
        music4.setId(4);
        music4.setArtist("Justin Bieber");
        music4.setSong("Fourth Song");
        music4.setArtistImg(R.drawable.justin);
        music4.setSongImg(R.drawable.justin_bieber_pic);
        music4.setMusic(R.raw.justin_bieber4);
        list.add(music4);

        return list;
    }

    public static String convertMilliSecond(long milliSecond) {
        long second = (milliSecond / 1000) % 60;
        long minute = (milliSecond / (1000 * 60)) % 60;
        return String.format(Locale.US, "%02d:%02d", minute, second);
    }
}
