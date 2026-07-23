package com.sddevops.jenkins_maven.eclipse;

import java.util.Comparator;
import java.util.Objects;

public class Song {
	private String id;
	private String title;
	private String artiste;
	private double songLength;

	public Song(String id, String title, String artiste, double songLength) {
		super();
		this.id = id;
		this.title = title;
		this.artiste = artiste;
		this.songLength = songLength;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtiste() {
		return artiste;
	}

	public void setArtiste(String artiste) {
		this.artiste = artiste;
	}

	public double getSongLength() {
		return songLength;
	}

	public void setSongLength(double songLength) {
		this.songLength = songLength;
	}

	@Override
	public int hashCode() {
		return Objects.hash(artiste, id, songLength, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Song))
			return false;
		Song other = (Song) obj;
		return Objects.equals(artiste, other.artiste) && Objects.equals(id, other.id)
				&& Double.doubleToLongBits(songLength) == Double.doubleToLongBits(other.songLength)
				&& Objects.equals(title, other.title);
	}

	public static Comparator<Song> titleComparator = new Comparator<Song>() {
		@Override
		public int compare(Song s1, Song s2) {
			return (int) (s1.getTitle().compareTo(s2.getTitle()));
		}
	};

	public static Comparator<Song> songLengthComparator = new Comparator<Song>() {
		@Override         
		public int compare(Song s1, Song s2) {              
			return (s2.getSongLength() < s1.getSongLength() ? -1 :              
				(s2.getSongLength() == s1.getSongLength() ? 0 : 1));            
		}     
	};
	
	@Override
	public String toString() {
		return this.title + " by " + this.artiste;
	}
}