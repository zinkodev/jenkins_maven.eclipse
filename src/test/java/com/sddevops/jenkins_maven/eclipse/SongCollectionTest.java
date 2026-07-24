package com.sddevops.jenkins_maven.eclipse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SongCollectionTest {

	private SongCollection sc;
	private Song s1;
	private Song s2;
	private Song s3;
	private Song s4;
	private final int SONG_COLLECTION_SIZE = 0;
	private SongCollection sc_with_size;
	private SongCollection sc_with_size_1;

	@BeforeEach
	void setUp() throws Exception {
		sc = new SongCollection();
		s1 = new Song("001", "good 4 u", "Olivia Rodrigo", 3.59);
		s2 = new Song("002", "Peaches", "Justin Bieber", 3.18);
		s3 = new Song("003", "MONTERO", "Lil Nas", 2.3);
		s4 = new Song("004", "bad guy", "billie eilish", 3.14);
		sc.addSong(s1);
		sc.addSong(s2);
		sc.addSong(s3);
		sc.addSong(s4);
		sc_with_size = new SongCollection(5);
		sc_with_size_1 = new SongCollection(1);
	}

	@AfterEach
	void tearDown() throws Exception {
		sc = null;
		sc_with_size = null;
		sc_with_size_1 = null;
	}

	@Test
	void testGetSongs() {
		List<Song> testSc = sc.getSongs();
		assertEquals(testSc.size(), SONG_COLLECTION_SIZE);
	}

	@Test
	void testAddSong() {
		List<Song> testSc = sc.getSongs();
		assertEquals(testSc.size(), SONG_COLLECTION_SIZE);
		sc.addSong(s1);
		assertEquals(testSc.size(), SONG_COLLECTION_SIZE + 1);

		sc_with_size_1.addSong(s1);
		sc_with_size_1.addSong(s2);
		sc_with_size_1.addSong(s3);
		assertEquals(sc_with_size_1.getSongs().size(), 1);
	}

	@Test
	void testSortSongsByTitle() {
		List<Song> sortedSongList = sc.sortSongsByTitle();
		assertEquals(sortedSongList.get(0).getTitle(), "MONTERO");
		assertEquals(sortedSongList.get(1).getTitle(), "Peaches");
		assertEquals(sortedSongList.get(2).getTitle(), "bad guy");
		assertEquals(sortedSongList.get(3).getTitle(), "good 4 u");
	}

	@Test
	void testSortSongsBySongLength() {
		List<Song> sortedSongByLengthList = sc.sortSongsBySongLength();
		assertEquals(sortedSongByLengthList.get(0).getSongLength(), 3.59);
		assertEquals(sortedSongByLengthList.get(1).getSongLength(), 3.18);
		assertEquals(sortedSongByLengthList.get(2).getSongLength(), 3.14);
		assertEquals(sortedSongByLengthList.get(3).getSongLength(), 2.3);
	}

	@Test
	void testFindSongsById() {
		Song song = sc.findSongsById("004");
		assertEquals(song.getArtiste(), "billie eilish");
		assertNull(sc.findSongsById("doesnt exist"));
	}

	@Test
	void testFindSongByTitle() {
		Song song = sc.findSongByTitle("MONTERO");
		assertEquals(song.getArtiste(), "Lil Nas");
		assertNull(sc.findSongByTitle("doesnt exist"));
	}


	// PART 2: MOCKITO TESTING COVERAGE
	

	/**
	 * Part 2: Test valid API response parsing using Mockito Spy
	 * Covers: positive branch — valid JSON parsed correctly
	 */
	@Test
	void testFetchSongOfTheDay() {
		String mockJson = """
		{
			"id": "001",
			"title": "Mock Song",
			"artiste": "Mock Artist",
			"songLength": 4.25
		}
		""";

		SongCollection collection = spy(new SongCollection());
		doReturn(mockJson).when(collection).fetchSongJson();

		Song result = collection.fetchSongOfTheDay();

		// Verify fetchSongJson() was called exactly once
		verify(collection, times(1)).fetchSongJson();

		// Verify returned Song fields
		assertNotNull(result);
		assertEquals("001", result.getId());
		assertEquals("Mock Song", result.getTitle());
		assertEquals("Mock Artist", result.getArtiste());
		assertEquals(4.25, result.getSongLength());
	}

	/**
	 * Part 2: Test branch when API returns null
	 * Covers: if (jsonStr == null) → return null
	 */
	@Test
	void testInvalidFetchSongOfTheDay() {
		SongCollection collection = spy(new SongCollection());
		doReturn(null).when(collection).fetchSongJson();

		Song result = collection.fetchSongOfTheDay();

		verify(collection, times(1)).fetchSongJson();
		assertNull(result);
		assertEquals(0, collection.getSongs().size());
	}

	/**
	 * Part 2: Test catch block when API throws exception
	 * Covers: catch block → return null, collection unchanged
	 */
	@Test
	void testExceptionHandlingInFetchSongOfTheDay() {
		SongCollection collection = spy(new SongCollection());
		doThrow(new RuntimeException("API failed")).when(collection).fetchSongJson();

		Song result = collection.fetchSongOfTheDay();

		verify(collection, times(1)).fetchSongJson();
		assertNull(result);
		assertEquals(0, collection.getSongs().size());
	}

	
	// PART 3: ARTISTE BRANCH COVERAGE
	

	/**
	 * Part 3: Taylor Swift → converted to "TS" and added to collection
	 * Covers: if (artiste.equals("Taylor Swift")) branch
	 */
	@Test
	void testFetchSongTaylorSwift_ConvertsToTS_AndAddsToCollection() {
		SongCollection collection = spy(new SongCollection(3));
		String taylorJson = """
		{
			"id": "101",
			"title": "Blank Space",
			"artiste": "Taylor Swift",
			"songLength": 3.51
		}
		""";
		doReturn(taylorJson).when(collection).fetchSongJson();

		Song result = collection.fetchSongOfTheDay();

		verify(collection, times(1)).fetchSongJson();
		assertNotNull(result);
		assertEquals("TS", result.getArtiste());
		assertEquals(1, collection.getSongs().size());
	}

	/**
	 * Part 3: Bruno Mars → converted to "BM" and added to collection
	 * Covers: else if (artiste.equals("Bruno Mars")) branch
	 */
	@Test
	void testFetchSongBrunoMars_ConvertsToBM_AndAddsToCollection() {
		SongCollection collection = spy(new SongCollection(3));
		String brunoJson = """
		{
			"id": "102",
			"title": "Uptown Funk",
			"artiste": "Bruno Mars",
			"songLength": 4.30
		}
		""";
		doReturn(brunoJson).when(collection).fetchSongJson();

		Song result = collection.fetchSongOfTheDay();

		verify(collection, times(1)).fetchSongJson();
		assertNotNull(result);
		assertEquals("BM", result.getArtiste());
		assertEquals(1, collection.getSongs().size());
	}

	/**
	 * Part 3: Other artist → NOT added to collection
	 * Covers: else branch (neither Taylor Swift nor Bruno Mars)
	 */
	@Test
	void testFetchOtherArtist_DoesNotAddToCollection() {
		SongCollection collection = spy(new SongCollection(3));
		String otherJson = """
		{
			"id": "103",
			"title": "Blinding Lights",
			"artiste": "The Weeknd",
			"songLength": 3.20
		}
		""";
		doReturn(otherJson).when(collection).fetchSongJson();

		Song result = collection.fetchSongOfTheDay();

		verify(collection, times(1)).fetchSongJson();
		assertNotNull(result);
		assertEquals("The Weeknd", result.getArtiste());
		assertEquals(0, collection.getSongs().size());
	}
}