package com.sddevops.jenkins_maven.eclipse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class SongCollection {

    private ArrayList<Song> songs = new ArrayList<>();
    private int capacity;

    public SongCollection() {
        this.capacity = 20;
    }

    public SongCollection(int capacity) {
        this.capacity = capacity;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void addSong(Song song) {
        if (songs.size() != capacity) {
            songs.add(song);
        }
    }

    public ArrayList<Song> sortSongsByTitle() {
        Collections.sort(songs, Song.titleComparator);
        return songs;
    }

    public ArrayList<Song> sortSongsBySongLength() {
        Collections.sort(songs, Song.songLengthComparator);
        return songs;
    }

    public Song findSongsById(String id) {
        for (Song s : songs) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }

    public Song findSongByTitle(String title) {
        for (Song s : songs) {
            if (s.getTitle().equals(title)) {
                return s;
            }
        }
        return null;
    }

    protected String fetchSongJson() {
        String urlString = "https://mocki.io/v1/e1b14dea-d272-4b03-b102-252325168182";

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));

                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
                return response.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Song fetchSongOfTheDay() {
        try {
            String jsonStr = fetchSongJson();

            // Part 2: null API response
            if (jsonStr == null) {
                return null;
            }

            JSONObject json = new JSONObject(jsonStr);

            Song song = new Song(
                    json.getString("id"),
                    json.getString("title"),
                    json.getString("artiste"),
                    json.getDouble("songLength")
            );

            // Part 3: Taylor Swift is changed to TS and saved.
            if (song.getArtiste().equals("Taylor Swift")) {
                song = new Song(
                        song.getId(),
                        song.getTitle(),
                        "TS",
                        song.getSongLength()
                );

                addSong(song);

            // Part 3: Bruno Mars is changed to BM and saved.
            } else if (song.getArtiste().equals("Bruno Mars")) {
                song = new Song(
                        song.getId(),
                        song.getTitle(),
                        "BM",
                        song.getSongLength()
                );

                addSong(song);
            }

            // Other artistes are returned but not added to the collection.
            return song;

        } catch (Exception e) {
            System.out.println("API failed");
            return null;
        }
    }
}