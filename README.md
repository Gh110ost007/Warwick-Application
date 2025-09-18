# Warwick+ Film Database — Custom Data Structures Implementation

A hfilm database backend implemented from scratch using custom data structures, developed as part of CS126: Design of Information Structures at the University of Warwick.

## Project Overview

This project demonstrates advanced understanding of fundamental computer science concepts by implementing a complete film database system using only custom-built data structures. The system manages three core files — Movies, Credits, and Ratings — with optimized query performance and memory efficiency.

**Key Achievement:** Achieved 22x performance improvement (2800ms → 125ms) through strategic data structure design and algorithmic optimization.

## Technical Implementation

### Custom Data Structures

**MyHashTable**: A chaining-based hash table implementation providing O(1) average-case lookup, insertion, and deletion operations. Features collision resolution through linked list chaining and optimized for integer key hashing.

**MyDynamicArray**: A resizable array implementation with automatic capacity management, providing O(1) random access and amortized O(1) insertion. Implements efficient memory management with strategic resizing policies.

### Architecture Highlights

- **Constraint-Driven Design**: Built without Java Collections framework to demonstrate deep understanding of fundamental data structures
- **Performance-First Approach**: Achieved near-constant time operations for core database queries
- **Memory Efficiency**: Optimized storage with sparse data representation and intelligent resizing
- **Scalable Design**: Architecture supports efficient handling of large datasets

## System Architecture

### Movies Store

- **Primary Structure**: `MyHashTable<Integer, Movie>` for O(1) movie lookup by ID
- **Secondary Structure**: `MyDynamicArray<Integer>` for efficient iteration and search operations
- **Key Features**: Generic field access methods, collection management, production company tracking
- **Performance**: Constant-time movie retrieval, linear-time search operations

### Credits Store

- **Primary Structure**: `MyHashTable<Integer, CreditRecord>` for film-to-credits mapping
- **Global Indexes**: Separate dynamic arrays for unique cast and crew members
- **Key Features**: Duplicate prevention, billing order management, cross-film queries
- **Performance**: O(1) credit lookup, efficient global people searches

### Ratings Store

- **Dual Indexing**: Two hash tables for bidirectional queries
  - `movieID → ratings[]` for movie-based analytics
  - `userID → ratings[]` for user-based analytics
- **Key Features**: Rating aggregation, user behavior analysis, movie popularity metrics
- **Performance**: O(1) rating access from both movie and user perspectives

## Technical Skills Demonstrated

### Data Structure Design

- **Hash Table Implementation**: Custom chaining-based hash table with collision resolution
- **Dynamic Array Management**: Self-implemented resizable arrays with strategic memory allocation
- **Algorithm Optimization**: Achieved 22x performance improvement through data structure selection
- **Memory Management**: Efficient storage patterns and intelligent resizing policies

### Software Engineering Practices

- **Clean Architecture**: Separation of concerns with dedicated store classes
- **Code Reusability**: Generic helper methods reducing code duplication
- **Performance Analysis**: Time complexity optimization and benchmarking
- **Testing**: Comprehensive JUnit test suite covering edge cases and performance metrics

### Problem-Solving Approach

- **Constraint-Driven Development**: Built complex system without standard library dependencies
- **Trade-off Analysis**: Balanced performance, memory usage, and code complexity
- **Scalability Considerations**: Designed for efficient handling of large datasets
- **Iterative Improvement**: Performance optimization through systematic analysis

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

## Future Enhancements

### Scalability Improvements

- **Dynamic Hash Table Resizing**: Implement load factor monitoring and automatic rehashing
- **Advanced Sorting**: Replace O(n²) algorithms with O(n log n) alternatives (merge sort, quick sort)
- **Name-Based Indexing**: Add secondary hash tables for O(1) name-based searches
- **Memory Optimization**: Implement 1.5x growth factor for more efficient memory usage

### Production Readiness

- **Exception Handling**: Custom exception classes for better error management
- **Input Validation**: Comprehensive validation for all public methods
- **Caching Layer**: LRU cache implementation for frequently accessed data
- **Batch Operations**: Support for bulk insert/update operations

## Key Learnings & Impact

### Technical Growth

This project provided hands-on experience with fundamental computer science concepts including hash table collision resolution, dynamic array memory management, and algorithmic complexity analysis. The constraint of building without standard libraries deepened understanding of underlying data structure mechanics.

### Performance Achievement

The 22x performance improvement demonstrates ability to identify bottlenecks and implement effective optimizations. This experience translates directly to real-world software development where performance optimization is crucial.

### Engineering Excellence

The project showcases clean code practices, comprehensive testing, and systematic problem-solving approach. The generic helper methods and modular design demonstrate understanding of software engineering best practices.

## Technologies & Tools

- **Language**: Java 21
- **Build System**: Gradle
- **Testing**: JUnit
- **Version Control**: Git
- **Development**: IntelliJ IDEA
- **Documentation**: Markdown, PDF reports

## Academic Context

This project was completed as part of CS126: Design of Information Structures at the University of Warwick (2024/25). The coursework focused on implementing fundamental data structures from scratch to demonstrate deep understanding of computer science principles.

**Coursework Report**: Detailed design analysis and implementation decisions are documented in `CS126 Coursework Report.pdf`.

## Contact & Portfolio

This project demonstrates proficiency in:

- **Data Structure Implementation**: Custom hash tables and dynamic arrays
- **Algorithm Design**: Performance optimization and complexity analysis
- **Software Engineering**: Clean architecture and testing practices
- **Problem Solving**: Constraint-driven development and trade-off analysis

For more projects and technical details, please visit my GitHub profile or contact me directly.
