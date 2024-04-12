# Manatee API

The following API is made for educational purposes only and does not provide any meaningful functionalities.

## Getting started

This project requires Java 21 to be installed.
For developers, Amazon Coretta or Eclipse Termium are recommended JDKs.

For development purposes only, the relational H2 database is initialized in the local runtime.
On the shutdown, the database is torn down. There is no other option to set a persistent database.

### For Linux users (bash)

```bash
./gradlew build # Generates OpenAPI models, builds the application and runs tests.
./gradlew bootRun # Starts the application on a local network. 
```

### For Windows users

```bash
gradlew build # Generates OpenAPI models, builds the application and runs tests.
gradlew bootRun # Starts the application on a local network. 
```


# Summary
| Question                                 | Answer |
|------------------------------------------|--------|
| Time  spent (h)                          | 12h    |
| Hardest task, (with reasoning)           | -      |
| Uncompleted tasks, if any                | -      |
| Additional dependencies (with reasoning) | -      | 


Summary: Successfully completed tasks including creating endpoint specification, designing database entity, implementing endpoint with state transition, and writing unit tests. Enhanced GET /applications to include interview info and seeded database. Challenges included ensuring seamless state transitions and designing efficient database schema. Learned valuable skills in API design and testing.
