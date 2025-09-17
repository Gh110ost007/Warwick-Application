package structures;

// Custom implementation of a basic hash table with chaining
public class MyHashTable<K, V> {
    private static final int INITIAL_CAPACITY = 2048; // Fixed size of the internal table

    private MyLinkedList<Entry<K, V>>[] table; // Array of linked lists (buckets)
    private int size; // Number of key-value pairs stored

    // Constructor to initialize the hash table
    @SuppressWarnings("unchecked")
    public MyHashTable() {
        table = new MyLinkedList[INITIAL_CAPACITY];
        for (int i = 0; i < table.length; i++) {
            table[i] = new MyLinkedList<>(); // Initialize each bucket as an empty linked list
        }
        size = 0;
    }

    // Hash function: ensures non-negative index and fits into table size
    private int hash(K key) {
        return (key.hashCode() & 0x7fffffff) % table.length;
    }

    // Inserts a new key-value pair, or updates the value if the key already exists
    public void put(K key, V value) {
        int index = hash(key);
        MyLinkedList<Entry<K, V>> bucket = table[index];

        // Search for the key in the bucket
        for (int i = 0; i < bucket.size(); i++) {
            Entry<K, V> entry = bucket.get(i);
            if (entry.key.equals(key)) {
                entry.value = value; // Key found, update value
                return;
            }
        }

        // Key not found, add new entry
        bucket.add(new Entry<>(key, value));
        size++;
    }

    // Retrieves the value associated with a key
    public V get(K key) {
        int index = hash(key);
        MyLinkedList<Entry<K, V>> bucket = table[index];

        // Search for the key in the bucket
        for (int i = 0; i < bucket.size(); i++) {
            Entry<K, V> entry = bucket.get(i);
            if (entry.key.equals(key))
                return entry.value;
        }

        return null; // Key not found
    }

    // Removes a key-value pair from the table
    public V remove(K key) {
        int index = hash(key);
        MyLinkedList<Entry<K, V>> bucket = table[index];

        // Search for the key and remove it
        for (int i = 0; i < bucket.size(); i++) {
            Entry<K, V> entry = bucket.get(i);
            if (entry.key.equals(key)) {
                bucket.remove(entry);
                size--;
                return entry.value; // Return the removed value
            }
        }

        return null; // Key not found
    }

    // Checks if a key exists in the table
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    // Returns the number of key-value pairs
    public int size() {
        return size;
    }

    // Returns all keys stored, assuming the keys are integers
    public int[] keys() {
        MyDynamicArray<Integer> result = new MyDynamicArray<>();
        for (MyLinkedList<Entry<K, V>> bucket : table) {
            for (int i = 0; i < bucket.size(); i++) {
                Entry<K, V> entry = bucket.get(i);
                if (entry.key instanceof Integer) { // Only add integer keys
                    result.add((Integer) entry.key);
                }
            }
        }

        int[] keys = new int[result.size()];
        for (int i = 0; i < result.size(); i++) {
            keys[i] = result.get(i);
        }
        return keys;
    }

    // Finds all integer keys whose values satisfy a given condition
    public int[] findAllMatchingIDs(CheckFunction<V> checker) {
        MyDynamicArray<Integer> result = new MyDynamicArray<>();
        for (MyLinkedList<Entry<K, V>> bucket : table) {
            for (int i = 0; i < bucket.size(); i++) {
                Entry<K, V> entry = bucket.get(i);
                if (checker.check(entry.value) && entry.key instanceof Integer) {
                    result.add((Integer) entry.key);
                }
            }
        }

        int[] ids = new int[result.size()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = result.get(i);
        }
        return ids;
    }

    // Private inner class to store key-value pairs
    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    // Interface to allow custom value checks (used in findAllMatchingIDs)
    public interface CheckFunction<V> {
        boolean check(V value);
    }
}
