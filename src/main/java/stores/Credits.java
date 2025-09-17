package stores;

import interfaces.*;
import structures.*;

// Implementation of the ICredits interface, managing cast and crew information for movies
public class Credits implements ICredits {
    private Stores stores;

    private MyHashTable<Integer, CreditRecord> creditRecords; // Maps film ID to its CreditRecord
    private MyDynamicArray<CastCredit> uniqueCast; // Stores unique cast members
    private MyDynamicArray<CrewCredit> uniqueCrew; // Stores unique crew members

    // Constructor initializes data structures
    public Credits(Stores stores) {
        this.stores = stores;
        creditRecords = new MyHashTable<>();
        uniqueCast = new MyDynamicArray<>();
        uniqueCrew = new MyDynamicArray<>();
    }

    // Adds a cast and crew entry for a movie
    @Override
    public boolean add(CastCredit[] cast, CrewCredit[] crew, int id) {
        if (creditRecords.containsKey(id))
            return false; // Prevent duplicates
        creditRecords.put(id, new CreditRecord(cast, crew));

        // Add unique cast members
        if (cast != null) {
            for (CastCredit c : cast) {
                if (!containsCast(c.getID()))
                    uniqueCast.add(c);
            }
        }

        // Add unique crew members
        if (crew != null) {
            for (CrewCredit c : crew) {
                if (!containsCrew(c.getID()))
                    uniqueCrew.add(c);
            }
        }

        return true;
    }

    // Removes a movie's credits
    @Override
    public boolean remove(int id) {
        return creditRecords.remove(id) != null;
    }

    // Gets the cast of a movie, sorted by billing order
    @Override
    public CastCredit[] getFilmCast(int filmID) {
        CreditRecord record = creditRecords.get(filmID);
        if (record == null || record.cast == null)
            return new CastCredit[0];

        CastCredit[] castCopy = record.cast.clone(); // Work on a copy
        insertionSortCastByOrder(castCopy);
        return castCopy;
    }

    // Gets the crew of a movie, sorted by ID
    @Override
    public CrewCredit[] getFilmCrew(int filmID) {
        CreditRecord record = creditRecords.get(filmID);
        if (record == null || record.crew == null)
            return new CrewCredit[0];

        CrewCredit[] crewCopy = record.crew.clone();
        insertionSortCrewByID(crewCopy);
        return crewCopy;
    }

    // Gets the size of the cast
    @Override
    public int sizeOfCast(int filmID) {
        CreditRecord record = creditRecords.get(filmID);
        return (record == null || record.cast == null) ? -1 : record.cast.length;
    }

    // Gets the size of the crew
    @Override
    public int sizeOfCrew(int filmID) {
        CreditRecord record = creditRecords.get(filmID);
        return (record == null || record.crew == null) ? -1 : record.crew.length;
    }

    // Returns all unique cast members
    @Override
    public Person[] getUniqueCast() {
        int len = uniqueCast.size();
        Person[] result = new Person[len];
        for (int i = 0; i < len; i++)
            result[i] = uniqueCast.get(i);
        return result;
    }

    // Returns all unique crew members
    @Override
    public Person[] getUniqueCrew() {
        int len = uniqueCrew.size();
        Person[] result = new Person[len];
        for (int i = 0; i < len; i++)
            result[i] = uniqueCrew.get(i);
        return result;
    }

    // Searches cast members by name
    @Override
    public Person[] findCast(String cast) {
        MyDynamicArray<Person> result = new MyDynamicArray<>();
        for (int i = 0; i < uniqueCast.size(); i++) {
            CastCredit c = uniqueCast.get(i);
            if (c.getName() != null && c.getName().contains(cast))
                result.add(c);
        }
        return toPersonArray(result);
    }

    // Searches crew members by name
    @Override
    public Person[] findCrew(String crew) {
        MyDynamicArray<Person> result = new MyDynamicArray<>();
        for (int i = 0; i < uniqueCrew.size(); i++) {
            CrewCredit c = uniqueCrew.get(i);
            if (c.getName() != null && c.getName().contains(crew))
                result.add(c);
        }
        return toPersonArray(result);
    }

    // Gets a cast member by their ID
    @Override
    public Person getCast(int castID) {
        for (int i = 0; i < uniqueCast.size(); i++) {
            CastCredit c = uniqueCast.get(i);
            if (c.getID() == castID)
                return c;
        }
        return null;
    }

    // Gets a crew member by their ID
    @Override
    public Person getCrew(int crewID) {
        for (int i = 0; i < uniqueCrew.size(); i++) {
            CrewCredit c = uniqueCrew.get(i);
            if (c.getID() == crewID)
                return c;
        }
        return null;
    }

    // Returns all films a cast member has appeared in
    @Override
    public int[] getCastFilms(int castID) {
        MyDynamicArray<Integer> result = new MyDynamicArray<>();
        int[] keys = creditRecords.keys();
        for (int filmID : keys) {
            CreditRecord r = creditRecords.get(filmID);
            if (r.cast != null) {
                for (CastCredit c : r.cast) {
                    if (c.getID() == castID) {
                        result.add(filmID);
                        break;
                    }
                }
            }
        }
        return toIntArray(result);
    }

    // Returns all films a crew member has worked on
    @Override
    public int[] getCrewFilms(int crewID) {
        MyDynamicArray<Integer> result = new MyDynamicArray<>();
        int[] keys = creditRecords.keys();
        for (int filmID : keys) {
            CreditRecord r = creditRecords.get(filmID);
            if (r.crew != null) {
                for (CrewCredit c : r.crew) {
                    if (c.getID() == crewID) {
                        result.add(filmID);
                        break;
                    }
                }
            }
        }
        return toIntArray(result);
    }

    // Gets all films where a cast member was a "star" (top 3 billing order)
    @Override
    public int[] getCastStarsInFilms(int castID) {
        MyDynamicArray<Integer> result = new MyDynamicArray<>();
        int[] keys = creditRecords.keys();
        for (int filmID : keys) {
            CreditRecord r = creditRecords.get(filmID);
            if (r.cast != null) {
                for (CastCredit c : r.cast) {
                    if (c.getID() == castID && c.getOrder() <= 3) {
                        result.add(filmID);
                        break;
                    }
                }
            }
        }
        return toIntArray(result);
    }

    // Returns the top cast members with the most credits
    @Override
    public Person[] getMostCastCredits(int numResults) {
        MyDynamicArray<CastCounter> counts = new MyDynamicArray<>();
        for (int i = 0; i < uniqueCast.size(); i++) {
            CastCredit c = uniqueCast.get(i);
            int count = getNumCastCredits(c.getID());
            counts.add(new CastCounter(c, count));
        }

        insertionSortCastCounter(counts); // Sort by number of credits descending

        int len = Math.min(numResults, counts.size());
        Person[] result = new Person[len];
        for (int i = 0; i < len; i++) {
            result[i] = counts.get(i).cast;
        }
        return result;
    }

    // Returns number of cast credits for a cast member
    @Override
    public int getNumCastCredits(int castID) {
        int count = 0;
        int[] keys = creditRecords.keys();
        for (int filmID : keys) {
            CreditRecord r = creditRecords.get(filmID);
            if (r.cast != null) {
                for (CastCredit c : r.cast) {
                    if (c.getID() == castID)
                        count++;
                }
            }
        }
        return count == 0 ? -1 : count;
    }

    @Override
    public int size() {
        return creditRecords.size();
    }

    // ---------- Helper Methods ----------

    // Checks if a cast member already exists
    private boolean containsCast(int id) {
        for (int i = 0; i < uniqueCast.size(); i++) {
            if (uniqueCast.get(i).getID() == id)
                return true;
        }
        return false;
    }

    // Checks if a crew member already exists
    private boolean containsCrew(int id) {
        for (int i = 0; i < uniqueCrew.size(); i++) {
            if (uniqueCrew.get(i).getID() == id)
                return true;
        }
        return false;
    }

    // Converts a dynamic array of persons to a regular array
    private Person[] toPersonArray(MyDynamicArray<Person> arr) {
        Person[] result = new Person[arr.size()];
        for (int i = 0; i < arr.size(); i++)
            result[i] = arr.get(i);
        return result;
    }

    // Converts a dynamic array of integers to a regular array
    private int[] toIntArray(MyDynamicArray<Integer> arr) {
        int[] result = new int[arr.size()];
        for (int i = 0; i < arr.size(); i++)
            result[i] = arr.get(i);
        return result;
    }

    // Sorts cast by billing order using insertion sort
    private void insertionSortCastByOrder(CastCredit[] cast) {
        for (int i = 1; i < cast.length; i++) {
            CastCredit key = cast[i];
            int j = i - 1;
            while (j >= 0 && cast[j].getOrder() > key.getOrder()) {
                cast[j + 1] = cast[j];
                j--;
            }
            cast[j + 1] = key;
        }
    }

    // Sorts crew by ID using insertion sort
    private void insertionSortCrewByID(CrewCredit[] crew) {
        for (int i = 1; i < crew.length; i++) {
            CrewCredit key = crew[i];
            int j = i - 1;
            while (j >= 0 && crew[j].getID() > key.getID()) {
                crew[j + 1] = crew[j];
                j--;
            }
            crew[j + 1] = key;
        }
    }

    // Sorts cast counters (for most credits) descending
    private void insertionSortCastCounter(MyDynamicArray<CastCounter> arr) {
        for (int i = 1; i < arr.size(); i++) {
            CastCounter key = arr.get(i);
            int j = i - 1;
            while (j >= 0 && arr.get(j).count < key.count) {
                arr.set(j + 1, arr.get(j));
                j--;
            }
            arr.set(j + 1, key);
        }
    }

    // Internal class to represent a film's credits
    private static class CreditRecord {
        CastCredit[] cast;
        CrewCredit[] crew;

        CreditRecord(CastCredit[] cast, CrewCredit[] crew) {
            this.cast = cast;
            this.crew = crew;
        }
    }

    // Internal class for counting cast credits
    private static class CastCounter {
        CastCredit cast;
        int count;

        CastCounter(CastCredit cast, int count) {
            this.cast = cast;
            this.count = count;
        }
    }
}
