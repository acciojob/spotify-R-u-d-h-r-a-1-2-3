package com.driver;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class SpotifyService {

    //Auto-wire will not work in this case, no need to change this and add autowire

    SpotifyRepository spotifyRepository = new SpotifyRepository();

    public User CreateUser(String name, String mobile){
        return spotifyRepository.createUser(name, mobile);
    }

    public Artist CreateArtist(String name) {
        return spotifyRepository.createArtist(name);
    }

    public Album CreateAlbum(String title, String artistName) {
        return spotifyRepository.createAlbum(title, artistName);
    }

    public Song CreateSong(String title, String albumName, int length) throws Exception {
        return spotifyRepository.createSong(title, albumName, length);
    }

    public Playlist CreatePlaylistOnLength(String mobile, String title, int length) throws Exception {
        return spotifyRepository.createPlaylistOnLength(mobile, title, length);
    }

    public Playlist CreatePlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        return spotifyRepository.createPlaylistOnName(mobile, title, songTitles);
    }

    public Playlist FindPlaylist(String mobile, String playlistTitle) throws Exception {
        return spotifyRepository.findPlaylist(mobile, playlistTitle);
    }

    public Song LikeSong(String mobile, String songTitle) throws Exception {
        return spotifyRepository.likeSong(mobile,songTitle);
    }

    public String MostPopularArtist() {
        return spotifyRepository.mostPopularArtist();
    }

    public String MostPopularSong() {
        return spotifyRepository.mostPopularSong();
    }
}
