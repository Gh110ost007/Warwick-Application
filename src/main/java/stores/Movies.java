package stores;

import java.time.LocalDate;
import interfaces.IMovies;
import structures.*;

// Movies class that implements IMovies interface
// Responsible for storing and managing movie information
public class Movies implements IMovies {
    Stores stores; // Reference to the overall Stores object (so we can access other stores if
                   // needed)

    // A hashtable to map a movie's ID to its Movie object for quick lookup
    private MyHashTable<Integer, Movie> movieTable;

    // A dynamic array to keep track of all movie IDs in the order they were added
    private MyDynamicArray<Integer> movieIDs;

    // Constructor - initializes data structures
    public Movies(Stores stores) {
        this.stores = stores;
        movieTable = new MyHashTable<>();
        movieIDs = new MyDynamicArray<>();
    }

    // Adds a new movie to the store
    @Override
    public boolean add(int id, String title, String originalTitle, String overview, String tagline, String status,
            Genre[] genres, LocalDate release, long budget, long revenue, String[] languages,
            String originalLanguage, double runtime, String homepage, boolean adult, boolean video,
            String poster) {
        // Check if movie with same ID already exists
        if (movieTable.containsKey(id))
            return false;

        // Create a new Movie object with the provided details
        Movie movie = new Movie(id, title, originalTitle, overview, tagline, status, genres, release, budget, revenue,
                languages, originalLanguage, runtime, homepage, adult, video, poster);

        // Add movie to hashtable and record its ID
        movieTable.put(id, movie);
        movieIDs.add(id);
        return true;
    }

    // Removes a movie by ID
    @Override
    public boolean remove(int id) {
        // Only remove if the movie exists
        if (!movieTable.containsKey(id))
            return false;

        movieTable.remove(id);
        movieIDs.removeByValue(id); // Also remove the ID from the list of movieIDs
        return true;
    }

    // Returns a list of all movie IDs
    @Override
    public int[] getAllIDs() {
        int[] ids = new int[movieIDs.size()];
        for (int i = 0; i < movieIDs.size(); i++) {
            ids[i] = movieIDs.get(i);
        }
        return ids;
    }

    // Returns IDs of movies released between two dates
    @Override
    public int[] getAllIDsReleasedInRange(LocalDate start, LocalDate end) {
        MyDynamicArray<Integer> result = new MyDynamicArray<>();

        for (int i = 0; i < movieIDs.size(); i++) {
            Movie m = movieTable.get(movieIDs.get(i));

            // Check if the movie exists and has a release date
            if (m != null && m.release != null) {
                if (m.release.isAfter(start) && m.release.isBefore(end)) {
                    result.add(m.id);
                }
            }
        }

        // Convert result to static array
        int[] res = new int[result.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = result.get(i);
        }
        return res;
    }

    // ---------- Getters ----------

    @Override
    public String getTitle(int id) {
        return getField(id, m -> m.title);
    }

    @Override
    public String getOriginalTitle(int id) {
        return getField(id, m -> m.originalTitle);
    }

    @Override
    public String getOverview(int id) {
        return getField(id, m -> m.overview);
    }

    @Override
    public String getTagline(int id) {
        return getField(id, m -> m.tagline);
    }

    @Override
    public String getStatus(int id) {
        return getField(id, m -> m.status);
    }

    @Override
    public Genre[] getGenres(int id) {
        return getField(id, m -> m.genres);
    }

    @Override
    public LocalDate getRelease(int id) {
        return getField(id, m -> m.release);
    }

    @Override
    public long getBudget(int id) {
        return getField(id, m -> m.budget, -1L);
    }

    @Override
    public long getRevenue(int id) {
        return getField(id, m -> m.revenue, -1L);
    }

    @Override
    public String[] getLanguages(int id) {
        return getField(id, m -> m.languages);
    }

    @Override
    public String getOriginalLanguage(int id) {
        return getField(id, m -> m.originalLanguage);
    }

    @Override
    public double getRuntime(int id) {
        return getField(id, m -> m.runtime, -1.0);
    }

    @Override
    public String getHomepage(int id) {
        return getField(id, m -> m.homepage);
    }

    @Override
    public boolean getAdult(int id) {
        return getField(id, m -> m.adult, false);
    }

    @Override
    public boolean getVideo(int id) {
        return getField(id, m -> m.video, false);
    }

    @Override
    public String getPoster(int id) {
        return getField(id, m -> m.poster);
    }

    @Override
    public double getVoteAverage(int id) {
        return getField(id, m -> m.voteAverage, -1.0);
    }

    @Override
    public int getVoteCount(int id) {
        return getField(id, m -> m.voteCount, -1);
    }

    @Override
    public String getIMDB(int filmID) {
        return getField(filmID, m -> m.imdbID);
    }

    @Override
    public double getPopularity(int id) {
        return getField(id, m -> m.popularity, -1.0);
    }

    @Override
    public int getCollectionID(int filmID) {
        return getField(filmID, m -> m.collectionID, -1);
    }

    // ---------- Setters to modify movie details ----------

    @Override
    public boolean setVote(int id, double voteAverage, int voteCount) {
        Movie m = movieTable.get(id);
        if (m == null)
            return false;
        m.voteAverage = voteAverage;
        m.voteCount = voteCount;
        return true;
    }

    @Override
    public boolean setIMDB(int filmID, String imdbID) {
        Movie m = movieTable.get(filmID);
        if (m == null)
            return false;
        m.imdbID = imdbID;
        return true;
    }

    @Override
    public boolean setPopularity(int id, double popularity) {
        Movie m = movieTable.get(id);
        if (m == null)
            return false;
        m.popularity = popularity;
        return true;
    }

    @Override
    public boolean addToCollection(int filmID, int collectionID, String name, String posterPath, String backdropPath) {
        // Attach movie to a collection
        Movie m = movieTable.get(filmID);
        if (m == null)
            return false;
        m.collectionID = collectionID;
        m.collectionName = name;
        m.collectionPosterPath = posterPath;
        m.collectionBackdropPath = backdropPath;
        return true;
    }

    // ---------- Handling movie collections ----------

    @Override
    public int[] getFilmsInCollection(int collectionID) {
        MyDynamicArray<Integer> result = new MyDynamicArray<>();

        // Loop through all movies to find ones belonging to this collection
        for (int i = 0; i < movieIDs.size(); i++) {
            Movie m = movieTable.get(movieIDs.get(i));
            if (m != null && m.collectionID == collectionID) {
                result.add(m.id);
            }
        }

        int[] res = new int[result.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = result.get(i);
        }
        return res;
    }

    @Override
    public String getCollectionName(int collectionID) {
        for (int i = 0; i < movieIDs.size(); i++) {
            Movie m = movieTable.get(movieIDs.get(i));
            if (m != null && m.collectionID == collectionID) {
                return m.collectionName;
            }
        }
        return null;
    }

    @Override
    public String getCollectionPoster(int collectionID) {
        for (int i = 0; i < movieIDs.size(); i++) {
            Movie m = movieTable.get(movieIDs.get(i));
            if (m != null && m.collectionID == collectionID) {
                return m.collectionPosterPath;
            }
        }
        return null;
    }

    @Override
    public String getCollectionBackdrop(int collectionID) {
        for (int i = 0; i < movieIDs.size(); i++) {
            Movie m = movieTable.get(movieIDs.get(i));
            if (m != null && m.collectionID == collectionID) {
                return m.collectionBackdropPath;
            }
        }
        return null;
    }

    // ---------- Managing Production Companies and Countries ----------

    @Override
    public boolean addProductionCompany(int id, Company c) {
        Movie m = movieTable.get(id);
        if (m == null)
            return false;
        m.productionCompanies.add(c);
        return true;
    }

    @Override
    public boolean addProductionCountry(int id, String c) {
        Movie m = movieTable.get(id);
        if (m == null)
            return false;
        m.productionCountries.add(c);
        return true;
    }

    @Override
    public Company[] getProductionCompanies(int id) {
        Movie m = movieTable.get(id);
        if (m == null)
            return null;
        Company[] arr = new Company[m.productionCompanies.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = m.productionCompanies.get(i);
        }
        return arr;
    }

    @Override
    public String[] getProductionCountries(int id) {
        Movie m = movieTable.get(id);
        if (m == null)
            return null;
        String[] arr = new String[m.productionCountries.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = m.productionCountries.get(i);
        }
        return arr;
    }

    // Returns the number of movies stored
    @Override
    public int size() {
        return movieIDs.size();
    }

    // Finds all movies matching a search term in title/original title/overview
    @Override
    public int[] findFilms(String searchTerm) {
        MyDynamicArray<Integer> result = new MyDynamicArray<>();
        for (int i = 0; i < movieIDs.size(); i++) {
            Movie m = movieTable.get(movieIDs.get(i));
            if (m == null)
                continue;

            if ((m.title != null && m.title.contains(searchTerm)) ||
                    (m.originalTitle != null && m.originalTitle.contains(searchTerm)) ||
                    (m.overview != null && m.overview.contains(searchTerm))) {
                result.add(m.id);
            }
        }

        int[] res = new int[result.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = result.get(i);
        }
        return res;
    }

    // ---------- Internal helper methods ----------

    private <T> T getField(int id, java.util.function.Function<Movie, T> extractor) {
        Movie m = movieTable.get(id);
        return (m != null) ? extractor.apply(m) : null;
    }

    private <T> T getField(int id, java.util.function.Function<Movie, T> extractor, T defaultValue) {
        Movie m = movieTable.get(id);
        return (m != null) ? extractor.apply(m) : defaultValue;
    }

    // Private class representing a single Movie
    private static class Movie {
        int id;
        String title, originalTitle, overview, tagline, status, originalLanguage, homepage, poster, imdbID;
        String collectionName, collectionPosterPath, collectionBackdropPath;
        Genre[] genres;
        LocalDate release;
        long budget, revenue;
        String[] languages;
        double runtime, voteAverage, popularity;
        int voteCount, collectionID;
        boolean adult, video;
        MyDynamicArray<Company> productionCompanies = new MyDynamicArray<>();
        MyDynamicArray<String> productionCountries = new MyDynamicArray<>();

        Movie(int id, String title, String originalTitle, String overview, String tagline, String status,
                Genre[] genres, LocalDate release, long budget, long revenue, String[] languages,
                String originalLanguage,
                double runtime, String homepage, boolean adult, boolean video, String poster) {
            this.id = id;
            this.title = title;
            this.originalTitle = originalTitle;
            this.overview = overview;
            this.tagline = tagline;
            this.status = status;
            this.genres = genres;
            this.release = release;
            this.budget = budget;
            this.revenue = revenue;
            this.languages = languages;
            this.originalLanguage = originalLanguage;
            this.runtime = runtime;
            this.homepage = homepage;
            this.adult = adult;
            this.video = video;
            this.poster = poster;
            this.collectionID = -1; // Default collection ID (means no collection initially)
        }
    }
}