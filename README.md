# Ranking
Java 11
## How to build
#### Using Gradle
Execute following terminal commands.
```
./gradlew build
./gradlew run
```
#### Inside IntelliJ
Just open the project and play the main program.
## Details
### Hexagonal Architecture:
The Architecture approach is based on Hexagonal Architecture - Ports and Adapters
Architecture, also tried to be Domain-Driven.

You will see the following folder structure inside the main folder:
- **configurationAdapters**: Where dependency injection is made and the main program is executed.
- **domain**: Where the core-domain of our business is built. Following Hexagonal Architecture, nothing inside the domain
should now anything about the outside (HttpServer, database, ...)
    - **model**: Where data models from our business are written.
    - **ports**:
      - incoming ports expose interfaces to the outside to communicate with the domain.
      - outcoming ports expose interfaces used by the domain to communicate with the outside.
      - Following this approach, we can make our services communicate with an InMemory BBDD in a decoupled way.
    - **services**: Where our business logic - use cases are implemented.
- **infrastructure**: Here we will find the implementation about the outside or details that the business
logic shouldn't know about, such as the HTTP Server and Databases.

### Ranking Data Structure:
The data structure chosen for the Ranking is a TreeSet, allowing us to add and remove at O(log n), while
maintaining it sorted at the same cost.
Searches inside the TreeSet are made by copying content to an Array, and then performing Binary Search at O(log n) cost.
Copying the content to an Array in every search is something that I would have preferred to avoid.
Maybe combining a Read Data Structure as a Map, and a Write Data Structure as a Sorted Array, could have been another
good option.

This data structure is inside the Ranking domain model, containing a TreeSet of RankingUserScore.

### Business Logic:
Business Logic is performed by 
```
RankingServiceImpl

Ranking getOrCreate(int level);
void addRankingUserScore(RankingUserScore rankingUserScore);
``` 
### Unit Testing:
Unit Testing was made by using mockito to work with mocks, jupiter and assertJ as assertion library.
Tests are written following this naming convention:
`Given_SomeSituation_When_aMethodIsExecuted_Then_SomethingHappens`.

And the implementation is as follows:
```
Given_When_Then() {
    //Declaration of test representative needed variables (Non representative has the ANY prefix).
    //Mocking methods.
    //Performing method to test.
    //Assertions making.
}
```
### Concurrency:
Regarding Concurrency, I came up with two options:
- Using the keyword `synchronized` to force that only one thread executes a concrete section.
- Making use of Optimistic Locking to allow that reads are not blocked and then reading again
if the read turned to be dirty.

I chose the second option because I supposed that in a long-living Ranking, more reads were supposed to be executed
than writes, as long as higher scores in a game get harder to achieve.

As the `synchronized` option is always blocking, tends to be less performant. But I found out that my
Optimistic Lock option sometimes gives undesired and unexpected results.
### Session Key:
- SessionKey is implemented as follows:
  - idUser is appended to a UUID, to be reasonably unique, and appended to a timestamp.
  - The result is encoded in Base64 to deliver a uniform result without strange characters.
  - That's not supposed to be suitable for a real production environment.
### Postman
You will find an exported postman collection to test the endpoints easily.

