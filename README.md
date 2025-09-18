# Warwick+ Coursework — CS126 (2024/25)

## Introduction

This project was developed as part of the CS126: Design of Information Structures module at the University of Warwick. The aim was to implement backend store classes — Movies, Credits, and Ratings — using custom-built data structures without relying on Java's Collections framework.

The stores together form a simplified backend for a film database, supporting queries on movies, ratings, and cast/crew information. This report discusses the design and implementation of the three store classes, explaining the data structures used, the reasoning behind the choices, and how they helped meet the coursework requirements efficiently.

**Key Achievement:** Achieved 22x performance improvement (2800ms → 125ms) through strategic data structure design and optimization.

## Data Structures Overview

Across all three classes, the implementation mainly used two custom-built data structures:

- **MyHashTable**: a simple hash table based on chaining with linked lists
- **MyDynamicArray**: a manually implemented resizable array, similar to Java's built-in ArrayList

These structures provided fast access times, simplicity of implementation, and flexibility while staying within the coursework restrictions (i.e., not using Java's standard Collections framework). They were lightweight but powerful enough to handle all the basic operations the Warwick+ project required, such as inserting, removing, finding, and listing many entries efficiently.

## Implemented Stores

### Movies Class

**Structures Used:**

- `MyHashTable<Integer, Movie> movieTable`
- `MyDynamicArray<Integer> movieIDs`

**Reasoning:**
The system needed to quickly find a movie by its ID (for example, when setting ratings or popularity) and list all movies for tasks like searching by title, checking collections, etc. Using a hash table for movie lookup made sense because IDs were unique integers and the hash table provided constant time access. Keeping a separate dynamic array of movieIDs made iteration (e.g., getting all movies, searching by title) easier and efficient without scanning the whole hash table.

**Implementation:**
Each movie added to the store is stored in movieTable, and its ID is recorded in movieIDs. To avoid repeating similar code for each movie field (like title, overview, revenue, etc.), a generic getField helper was created that takes a lambda extractor. This design kept the class concise, clear, and less error-prone.

### Credits Class

**Structures Used:**

- `MyHashTable<Integer, CreditRecord> creditRecords`
- `MyDynamicArray<CastCredit> uniqueCast`
- `MyDynamicArray<CrewCredit> uniqueCrew`

**Reasoning:**
Credits management needed to quickly find the cast and crew for a film, find all films for a cast/crew member, and search people globally (across all movies). Using a hash table for mapping filmID to CreditRecord gave fast access to a movie's credits. Using dynamic arrays for uniqueCast and uniqueCrew allowed flexible global searching and listing.

**Implementation:**
Each film's cast and crew are stored together in a CreditRecord object linked to its filmID. When adding credits, the system ensures that no duplicate cast or crew members are inserted into the unique lists.

### Ratings Class

**Structures Used:**

- `MyHashTable<Integer, MyDynamicArray<Rating>> movieRatings`
- `MyHashTable<Integer, MyDynamicArray<Rating>> userRatings`

**Reasoning:**
The Ratings class needed to efficiently support two types of access: find all ratings for a movie and find all ratings made by a user. A hash table was the best fit because it allowed constant time O(1) access to either movieID or userID. A dynamic array was used for storing multiple ratings associated with each movie or user because the number of ratings is unpredictable and could grow without an initial fixed limit.

## Why These Data Structures Were Chosen

### MyHashTable

Hash tables were essential because:

- They offer O(1) average lookup time, compared to O(n) for arrays or lists
- They allowed direct access to the movie or user of interest without scanning
- Implementing a custom chaining-based hash table provided flexibility over collision handling and load control
- In all three stores, IDs (integers) made hashing very simple and efficient

### MyDynamicArray

Dynamic arrays were chosen because:

- They grow automatically when needed without worrying about capacity
- They offer O(1) random access by index
- For operations like listing all movies, listing cast/crew, and sorting results, they were the simplest solution
- Writing a custom array allowed understanding and control of resizing policies
- Compared to linked lists, dynamic arrays have better cache performance and faster access, which suited this coursework better

## Repository Structure

```
src/
├── main/java/
│   ├── stores/
│   │   ├── Movies.java          # Movie metadata management
│   │   ├── Credits.java         # Cast/crew credit management
│   │   ├── Ratings.java         # User rating management
│   │   └── Stores.java          # Main store coordinator
│   ├── structures/
│   │   ├── MyHashTable.java     # Custom hash table implementation
│   │   ├── MyDynamicArray.java  # Custom dynamic array
│   │   ├── MyLinkedList.java    # Custom linked list
│   │   └── MyArrayList.java     # Custom array list
│   ├── interfaces/              # Store interface definitions
│   ├── screen/                  # UI screen implementations
│   └── utils/                   # Utility classes
├── test/java/
│   ├── MoviesTest.java          # Movie store tests
│   ├── CreditsTest.java         # Credits store tests
│   └── RatingsTest.java         # Ratings store tests
data/                            # CSV data files
├── movies_metadata.csv
├── credits.csv
├── ratings.csv
└── keywords.csv
CS126 Coursework Report.pdf      # Detailed design report
```

## How to Run

### Prerequisites

- Java 21 or higher
- Gradle (or use the included wrapper)

### Build and Test

```bash
# Clone the repository
git clone https://github.com/Gh110ost007/Warwick-Application.git
cd Warwick-Application

# Build the project
./gradlew build

# Run tests
./gradlew test

# Run the application
./gradlew run
```

### Alternative: Using JAR

```bash
# Build JAR file
./gradlew jar

# Run the JAR
java -jar build/libs/WarwickPlus.jar
```

## Key Design Decisions

### Data Structure Choices

- **Hash Tables**: Chosen for O(1) average lookup time, essential for ID-based queries
- **Dynamic Arrays**: Selected for automatic resizing and O(1) random access
- **Dual Indexing**: Ratings store uses two hash tables for efficient bidirectional queries

### Code Quality

- **DRY Principle**: Generic `getField()` methods eliminate repetitive null-checking
- **Consistent Error Handling**: Standardized return values for missing data
- **Clear Separation**: Hash tables for lookup, dynamic arrays for iteration

## Testing

The project includes comprehensive JUnit tests covering:

- Store functionality (add, remove, get operations)
- Edge cases (null values, invalid IDs)
- Performance benchmarks
- Data integrity checks

Run tests with:

```bash
./gradlew test
```

## Performance Metrics

| Operation             | Time Complexity | Implementation Notes                    |
| --------------------- | --------------- | --------------------------------------- |
| Movie Lookup by ID    | O(1)            | Hash table direct access                |
| User Rating Lookup    | O(1)            | Hash table direct access                |
| Movie Search by Title | O(n)            | Linear scan with optimization potential |
| Collection Queries    | O(n)            | Linear scan with optimization potential |
| Rating Addition       | O(1)            | Hash table insertion                    |
| Data Population       | 125ms           | 22x improvement from initial 2800ms     |

## Possible Improvements

1. **Hash Table Resizing**
   Currently, the hash table has a fixed capacity (2048). If the number of stored entries grows too large, the hash table could become inefficient because of collisions. An improvement would be to track load factor (e.g., size / capacity) and resize (double capacity) and rehash all entries once a threshold is exceeded. This would maintain near O(1) access even for very large datasets.

2. **Better Sorting Algorithms**
   Bubble sort is simple but inefficient (O(n²)). For production-scale data, a stable and efficient sort like merge sort (O(n log n)) would perform much better. For numeric sorts, quick sort could be used where stability is not required.

3. **Indexing by Name**
   Searching by cast/crew name or movie title currently requires linear scans. A better version would involve building a second hash table mapping names -> IDs. This would make searching for names instant (O(1)) instead of O(n). This could especially help if the database contains thousands of movies and people.

4. **Memory Efficiency**
   MyDynamicArray doubles its size during resizing. This is fast, but wastes memory when only slightly over capacity. Strategies like increasing size by 1.5x would save memory while still keeping resizing cost low.

5. **Error Handling**
   Currently, invalid IDs return null or -1. A more robust system would throw custom exceptions (e.g., MovieNotFoundException) and provide clearer API behavior for error cases. This would make the store classes safer and more predictable to use.

## Reflections

When designing these classes, the main goals were simplicity and correctness. The approach focused on:

- Minimizing unnecessary complexity
- Writing code that is easy to read, debug, and explain
- Focusing on functionality first, then minor optimizations

During testing, it became clear that good internal helpers (like getField) saved a lot of repetitive work. This project provided hands-on experience with core data structure concepts like hashing, dynamic arrays, and sorting algorithms.

## Conclusion

Overall, the design and implementation achieved the intended goals. The combination of hash tables and dynamic arrays allowed the system to meet all the coursework functionality requirements, keep the code manageable, and achieve good performance within the limits of simple data structures.

If this project was expanded to a real-world application, time would definitely be invested into optimizing sort performance, improving lookup speeds, and reducing memory usage. However, for Warwick+ coursework, this balance of performance, simplicity, and correctness was the right choice.

## Documentation

- **CS126 Coursework Report.pdf** — Comprehensive design analysis and implementation details
- **Code Comments** — Extensive inline documentation explaining design decisions
- **Interface Documentation** — Clear API specifications in interface files
