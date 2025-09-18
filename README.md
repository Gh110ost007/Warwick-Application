# Warwick+ Coursework — CS126 (2024/25)

## 📌 Overview

This project was developed as part of the **CS126: Design of Information Structures** module at the University of Warwick.  
The aim was to implement backend store classes — `Movies`, `Credits`, and `Ratings` — using **custom-built data structures** without relying on Java's Collections framework.

The stores together form a simplified backend for a film database, supporting queries on movies, ratings, and cast/crew information.

---

## ⚙️ Tech Stack

- **Language:** Java (21)
- **Custom Data Structures:**
  - `MyHashTable<K, V>` — chaining-based hash table for O(1) lookups
  - `MyDynamicArray<T>` — resizable array for flexible storage
- **Build Tool:** Gradle
- **Testing:** JUnit

---

## 🏗️ Implemented Stores

### 🎬 Movies

- Stores all movie metadata (`title`, `overview`, `revenue`, `genres`, etc.)
- Fast O(1) lookup by `movieID` using `MyHashTable<Integer, Movie>`
- Sequential access via `MyDynamicArray<Integer>` of IDs for listing/search
- Supports collection management and production company/country tracking
- Generic `getField()` helper methods eliminate code duplication

### ⭐ Credits

- Manages cast and crew credits for each film
- Maps `filmID -> CreditRecord` using a hash table
- Maintains global unique lists of cast and crew in dynamic arrays
- Supports queries like _find stars in films_ or _list all films for a crew member_
- Handles billing order sorting and duplicate prevention

### 📊 Ratings

- Stores user ratings of movies with two hash tables:
  - `movieID -> ratings[]` for movie-based queries
  - `userID -> ratings[]` for user-based queries
- Enables queries like _all ratings for a movie_ or _all ratings by a user_
- Ratings can be added, updated, or removed efficiently
- Supports finding most-rated movies and users

---

## 🚀 Performance Achievements

- **Store population time** optimized from ~2800ms to ~125ms (~22x faster) through:
  - Efficient hashing with custom hash table implementation
  - Use of lightweight dynamic arrays
  - Reduced code duplication and improved data flow
  - Generic helper methods for consistent field access
- Achieved near O(1) lookups for movies, users, and ratings
- Memory-efficient storage with only non-empty entries

---

## 📂 Repository Structure

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

---

## ▶️ How to Run

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

---

## 🔍 Key Design Decisions

### Data Structure Choices

- **Hash Tables**: Chosen for O(1) average lookup time, essential for ID-based queries
- **Dynamic Arrays**: Selected for automatic resizing and O(1) random access
- **Dual Indexing**: Ratings store uses two hash tables for efficient bidirectional queries

### Code Quality

- **DRY Principle**: Generic `getField()` methods eliminate repetitive null-checking
- **Consistent Error Handling**: Standardized return values for missing data
- **Clear Separation**: Hash tables for lookup, dynamic arrays for iteration

---

## 🧪 Testing

The project includes comprehensive JUnit tests covering:

- Store functionality (add, remove, get operations)
- Edge cases (null values, invalid IDs)
- Performance benchmarks
- Data integrity checks

Run tests with:

```bash
./gradlew test
```

---

## 🔮 Future Improvements

### Performance Optimizations

- **Hash Table Resizing** — implement dynamic resizing & rehashing when load factor exceeds threshold
- **Efficient Sorting** — replace bubble sort/insertion sort with mergesort or quicksort for scalability
- **Indexing by Name** — add `HashTable<String, ID>` for O(1) search by names/titles
- **Memory Optimizations** — fine-tune resizing policies in `MyDynamicArray`

### Code Quality

- **Robust Error Handling** — custom exceptions for invalid queries instead of null returns
- **Input Validation** — comprehensive validation for all public methods
- **Documentation** — expand JavaDoc comments for better API documentation

### Features

- **Caching** — implement LRU cache for frequently accessed movies
- **Batch Operations** — support for bulk insert/update operations
- **Data Persistence** — save/load store state to/from files

---

## 📊 Performance Analysis

Based on the detailed coursework report, the implementation demonstrates:

| Operation             | Time Complexity | Notes                                 |
| --------------------- | --------------- | ------------------------------------- |
| Movie Lookup by ID    | O(1)            | Hash table access                     |
| User Rating Lookup    | O(1)            | Hash table access                     |
| Movie Search by Title | O(n)            | Linear scan (improvement opportunity) |
| Collection Queries    | O(n)            | Linear scan (improvement opportunity) |
| Rating Addition       | O(1)            | Hash table insertion                  |

---

## 📑 Documentation

- **CS126 Coursework Report.pdf** — Comprehensive design analysis and implementation details
- **Code Comments** — Extensive inline documentation explaining design decisions
- **Interface Documentation** — Clear API specifications in interface files

---

## 🤝 Contributing

This is a coursework project for CS126. If you're a fellow Warwick student working on similar coursework, feel free to:

- Study the implementation for learning purposes
- Suggest improvements or optimizations
- Report any issues you discover

---

## 📄 License

This project is created for educational purposes as part of CS126 coursework at the University of Warwick.

---

## 👨‍💻 Author

Developed as part of CS126 coursework at the University of Warwick (2024/25).

---

_For detailed implementation analysis and design decisions, see the included `CS126 Coursework Report.pdf`._
