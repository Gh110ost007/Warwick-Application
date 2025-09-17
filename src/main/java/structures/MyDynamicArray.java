package structures;

// Custom implementation of a dynamic array (similar to ArrayList)
public class MyDynamicArray<T> {
    private Object[] data; // Internal array to store elements
    private int size; // Number of elements currently in the array
    private int capacity; // Total available space before needing to resize

    // Default constructor: starts with capacity 10
    public MyDynamicArray() {
        this(10);
    }

    // Constructor with custom initial capacity
    public MyDynamicArray(int initialCapacity) {
        this.capacity = initialCapacity;
        this.data = new Object[capacity];
        this.size = 0;
    }

    // Adds a new element to the end of the array
    public void add(T value) {
        if (size == capacity)
            resize(); // If array is full, double its size
        data[size++] = value; // Insert value and increment size
    }

    // Sets a value at a specific index
    public void set(int index, T value) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        data[index] = value;
    }

    // Retrieves an element at a specific index
    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        return (T) data[index];
    }

    // Returns the number of elements currently stored
    public int size() {
        return size;
    }

    // Removes the first occurrence of a given value from the array
    public boolean removeByValue(T value) {
        for (int i = 0; i < size; i++) {
            if (data[i].equals(value)) {
                // Shift elements to fill the gap
                System.arraycopy(data, i + 1, data, i, size - i - 1);
                data[--size] = null; // Clean up last slot
                return true;
            }
        }
        return false; // Value not found
    }

    // Doubles the capacity of the internal array when full
    private void resize() {
        capacity *= 2;
        Object[] newData = new Object[capacity];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData; // Replace old array with the new one
    }
}
