package stores;

import java.time.LocalDateTime;
import interfaces.IRatings;
import structures.*;

public class Ratings implements IRatings {
    private Stores stores;

    // Hash table mapping movie IDs -> dynamic array of ratings for that movie
    private MyHashTable<Integer, MyDynamicArray<Rating>> movieRatings;

    // Hash table mapping user IDs -> dynamic array of ratings made by that user
    private MyHashTable<Integer, MyDynamicArray<Rating>> userRatings;

    // Constructor - creates empty rating store
    public Ratings(Stores stores) {
        this.stores = stores;
        this.movieRatings = new MyHashTable<>();
        this.userRatings = new MyHashTable<>();
    }

    // Private inner class to represent a single rating
    private static class Rating {
        int userID;
        int movieID;
        float rating;
        LocalDateTime timestamp;

        // Constructor for a Rating
        Rating(int userID, int movieID, float rating, LocalDateTime timestamp) {
            this.userID = userID;
            this.movieID = movieID;
            this.rating = rating;
            this.timestamp = timestamp;
        }
    }

    @Override
    public boolean add(int userID, int movieID, float rating, LocalDateTime timestamp) {
        // Check if this user already rated this movie (no duplicates allowed)
        if (containsRating(userID, movieID))
            return false;

        Rating r = new Rating(userID, movieID, rating, timestamp);

        // Insert into movieRatings table
        if (!movieRatings.containsKey(movieID)) {
            movieRatings.put(movieID, new MyDynamicArray<>());
        }
        movieRatings.get(movieID).add(r);

        // Insert into userRatings table
        if (!userRatings.containsKey(userID)) {
            userRatings.put(userID, new MyDynamicArray<>());
        }
        userRatings.get(userID).add(r);

        return true;
    }

    @Override
    public boolean remove(int userID, int movieID) {
        boolean removed = false;

        // Try to remove from movieRatings
        MyDynamicArray<Rating> mList = movieRatings.get(movieID);
        if (mList != null) {
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).userID == userID) {
                    mList.removeByValue(mList.get(i)); // Remove the rating object
                    removed = true;
                    break;
                }
            }
        }

        // Try to remove from userRatings
        MyDynamicArray<Rating> uList = userRatings.get(userID);
        if (uList != null) {
            for (int i = 0; i < uList.size(); i++) {
                if (uList.get(i).movieID == movieID) {
                    uList.removeByValue(uList.get(i)); // Remove the rating object
                    break;
                }
            }
        }

        return removed;
    }

    @Override
    public boolean set(int userID, int movieID, float rating, LocalDateTime timestamp) {
        // Try updating existing rating first
        MyDynamicArray<Rating> mList = movieRatings.get(movieID);
        if (mList != null) {
            for (int i = 0; i < mList.size(); i++) {
                Rating r = mList.get(i);
                if (r.userID == userID) {
                    r.rating = rating;
                    r.timestamp = timestamp;
                    return true;
                }
            }
        }
        // If not found, add a new rating
        return add(userID, movieID, rating, timestamp);
    }

    @Override
    public float[] getMovieRatings(int movieID) {
        // Return array of all ratings for a movie
        MyDynamicArray<Rating> mList = movieRatings.get(movieID);
        if (mList == null || mList.size() == 0)
            return new float[0];

        float[] res = new float[mList.size()];
        for (int i = 0; i < mList.size(); i++)
            res[i] = mList.get(i).rating;
        return res;
    }

    @Override
    public float[] getUserRatings(int userID) {
        // Return array of all ratings made by a user
        MyDynamicArray<Rating> uList = userRatings.get(userID);
        if (uList == null || uList.size() == 0)
            return new float[0];

        float[] res = new float[uList.size()];
        for (int i = 0; i < uList.size(); i++)
            res[i] = uList.get(i).rating;
        return res;
    }

    @Override
    public float getMovieAverageRating(int movieID) {
        // Calculate the average rating of a movie
        MyDynamicArray<Rating> mList = movieRatings.get(movieID);

        if (mList == null) {
            // If no ratings, check if movie exists
            if (stores.getMovies().getTitle(movieID) != null) {
                return 0.0f; // Movie exists but unrated
            } else {
                return -1.0f; // Movie doesn't exist
            }
        }

        if (mList.size() == 0)
            return 0.0f;

        float sum = 0;
        for (int i = 0; i < mList.size(); i++) {
            sum += mList.get(i).rating;
        }
        return sum / mList.size();
    }

    @Override
    public float getUserAverageRating(int userID) {
        // Calculate the average rating made by a user
        MyDynamicArray<Rating> uList = userRatings.get(userID);
        if (uList == null || uList.size() == 0)
            return -1.0f;

        float sum = 0;
        for (int i = 0; i < uList.size(); i++) {
            sum += uList.get(i).rating;
        }
        return sum / uList.size();
    }

    @Override
    public int[] getMostRatedMovies(int num) {
        // Return top num movies with the most ratings
        return getTopKeysByCount(movieRatings, num);
    }

    @Override
    public int[] getMostRatedUsers(int num) {
        // Return top num users who rated the most
        return getTopKeysByCount(userRatings, num);
    }

    private int[] getTopKeysByCount(MyHashTable<Integer, MyDynamicArray<Rating>> table, int num) {
        // Prepare array to store ID + count pairs
        int[] keys = table.keys();
        MyDynamicArray<int[]> counts = new MyDynamicArray<>();

        for (int id : keys) {
            int count = table.get(id).size();
            counts.add(new int[] { id, count });
        }

        // Bubble sort in descending order of counts
        for (int i = 0; i < counts.size() - 1; i++) {
            for (int j = 0; j < counts.size() - i - 1; j++) {
                if (counts.get(j)[1] < counts.get(j + 1)[1]) {
                    int[] temp = counts.get(j);
                    counts.set(j, counts.get(j + 1));
                    counts.set(j + 1, temp);
                }
            }
        }

        // Extract top num IDs
        int len = Math.min(num, counts.size());
        int[] result = new int[len];
        for (int i = 0; i < len; i++) {
            result[i] = counts.get(i)[0];
        }
        return result;
    }

    @Override
    public int getNumRatings(int movieID) {
        // Return number of ratings for a movie
        if (movieRatings.containsKey(movieID)) {
            return movieRatings.get(movieID).size();
        }
        // If movie exists but no ratings -> return 0
        return stores.getMovies().getTitle(movieID) != null ? 0 : -1;
    }

    @Override
    public int[] getTopAverageRatedMovies(int numResults) {
        // Find top numResults movies with highest average rating
        int[] keys = movieRatings.keys();
        MyDynamicArray<float[]> averages = new MyDynamicArray<>();

        for (int id : keys) {
            MyDynamicArray<Rating> mList = movieRatings.get(id);
            if (mList != null && mList.size() > 0) {
                float sum = 0;
                for (int i = 0; i < mList.size(); i++) {
                    sum += mList.get(i).rating;
                }
                averages.add(new float[] { id, sum / mList.size() });
            }
        }

        // Bubble sort by average rating descending
        for (int i = 0; i < averages.size() - 1; i++) {
            for (int j = 0; j < averages.size() - i - 1; j++) {
                if (averages.get(j)[1] < averages.get(j + 1)[1]) {
                    float[] temp = averages.get(j);
                    averages.set(j, averages.get(j + 1));
                    averages.set(j + 1, temp);
                }
            }
        }

        // Return top numResults movie IDs
        int len = Math.min(numResults, averages.size());
        int[] result = new int[len];
        for (int i = 0; i < len; i++) {
            result[i] = (int) averages.get(i)[0];
        }
        return result;
    }

    @Override
    public int size() {
        // Return total number of ratings in the system
        int total = 0;
        int[] keys = movieRatings.keys();
        for (int key : keys) {
            total += movieRatings.get(key).size();
        }
        return total;
    }

    // Helper method: Checks if a specific user has already rated a specific movie
    private boolean containsRating(int userID, int movieID) {
        MyDynamicArray<Rating> mList = movieRatings.get(movieID);
        if (mList != null) {
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).userID == userID)
                    return true;
            }
        }
        return false;
    }
}
