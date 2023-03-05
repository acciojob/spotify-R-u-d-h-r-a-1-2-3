package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        User u1 = new User(name, mobile);
        for(User i : users){
            if(i.getName().equals(name) && i.getMobile().equals(mobile)){
                return i;
            }
        }
        users.add(u1);
        return u1;
    }

    public Artist createArtist(String name) {
        Artist a1 = new Artist(name);
        for(Artist i : artists){
            if(i.getName().equals(name)){
                return i;
            }
        }
        artists.add(a1);
        return a1;
    }

    public Album createAlbum(String title, String artistName) {

        Artist ar = createArtist(artistName);
        Album al = new Album(title);

        albumSongMap.put(al,new ArrayList<Song>());

        albums.add(al);
        if(artistAlbumMap.containsKey(ar)){
            artistAlbumMap.get(ar).add(al);
        }else{
            List<Album> l = new ArrayList<>();
            l.add(al);
            artistAlbumMap.put(ar,l);
        }

        return al;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{

        boolean flag = false;
        Album temp = null;
        Song s1 = null;

        for(Album i : albums)
        {
            if(i.getTitle().equals(albumName)) {
                flag = true;
                temp = i;
            }
        }

        if(!flag)
            throw new Exception();
        else{
            s1 = new Song(title,length);
            songs.add(s1);
            albumSongMap.get(temp).add(s1);
        }
        return s1;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        Playlist p1 = new Playlist(title);;
        boolean flag = false;
        for(User i : users){
            if(i.getMobile().equals(mobile)){

                List<User> l = new ArrayList<>();
                l.add(i);
                playlistListenerMap.put(p1,l);

                creatorPlaylistMap.put(i,p1);

                flag = true;
                break;
            }
        }
        if(flag) {
            List<Song> l = new ArrayList<>();
            for (Song i : songs) {
                if (i.getLength() == length) {

                    l.add(i);
                }
            }
            playlistSongMap.put(p1,l);
        }else{
            throw new Exception();
        }
        return p1;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        Playlist p1 = new Playlist(title);;
        boolean flag = false;
        for(User i : users){
            if(i.getMobile().equals(mobile)){

                List<User> l = new ArrayList<>();
                l.add(i);
                playlistListenerMap.put(p1,l);

                creatorPlaylistMap.put(i,p1);

                flag = true;
                break;
            }
        }
        if(flag) {
            List<Song> l = new ArrayList<>();
            for (Song i : songs) {
                for(String j : songTitles)
                    if (i.getTitle().equals(j)) {
                        l.add(i);
                    }
            }
            playlistSongMap.put(p1,l);
        }else{
            throw new Exception();
        }
        return p1;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        boolean flag = false;
        User temp = null;
        Playlist temp2 = null;
        for(User i : users){
            if(i.getMobile().equals(mobile)){
                temp = i;
                flag = true;
                break;
            }
        }
        if(!flag) throw new Exception("User does not exist");

        flag = false;
        for (Playlist i : playlists) {
            if (i.getTitle().equals(playlistTitle)) {
                temp2 = i;
                flag = true;
                break;
            }
        }
        if(!flag) throw new Exception("Playlist does not exist");

        // If the user is creater or already a listener, do nothing
        if(creatorPlaylistMap.get(temp) == temp2){
            return temp2;
        }
        List<User> u = playlistListenerMap.get(temp2);
        for(User i : u){
            if(i == temp){
                return temp2;
            }
        }

        playlistListenerMap.get(temp2).add(temp);

        return temp2;


    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        boolean flag = false;
        User temp = null;
        Song temp2 = null;

        for(User i : users){
            if(i.getMobile().equals(mobile)){
                temp = i;
                flag = true;
                break;
            }
        }
        if(!flag) throw new Exception("User does not exist");

        flag = false;
        for (Song i : songs) {
            if (i.getTitle().equals(songTitle)) {
                temp2 = i;
                flag = true;
                break;
            }
        }
        if(!flag) throw new Exception("Song does not exist");

        temp2.setLikes(temp2.getLikes()+1);

        // setting like to artist
        Album alb = null;
        for(Album i : albumSongMap.keySet()){
            List<Song> list = albumSongMap.get(i);
            for(Song s : list){
                if(s.getTitle().equals(temp2.getTitle())){
                    alb = i;
                }
            }
        }

        Artist art = null;
        for(Artist i : artistAlbumMap.keySet()){
            List<Album> list = artistAlbumMap.get(i);
            for(Album s : list){
                if(s.getTitle().equals(temp2.getTitle())){
                    art = i;
                }
            }
        }
        art.setLikes(art.getLikes()+1);
        return temp2;

    }

    public String mostPopularArtist() {
        int max = 0;
        String output = "";
        for(Artist i : artists){
            if(i.getLikes() > max){
                output = i.getName();
            }
        }
        return output;
    }

    public String mostPopularSong() {
        int max = 0;
        String output = "";
        for(Song i : songs){
            if(i.getLikes() > max){
                output = i.getTitle();
            }
        }
        return output;
    }
}
