# Hanaeco Integration Test (Java/SpringBoot)

This is a Java-based SpringBoot application that consumes Hanaeco's API.

# Application Overview

This is a web application that uses Hanaeco's OpenAPI (former Swagger) spec to generate Java client codes.

The Hanaeco OpenAPI spec is located at
```declarative
src/main/resources/hanaeco-api-spec-1.8.yaml
```

When executed, it asks for Hanaeco base URL and API key.
Then data is properly entered, it can then fetch Organizations

## To generate code from OpenAPI spec:

```
mvn clean compile
```

> NOTE: In some cases `mvn clean compile` will fail, just try again.


## Running in debug mode

Intellij will allow debug the application by adding breakpoint and stepping.

As maven starts the application in a different process, it must be run separately and listen to the debug port:

Run the application from terminal:
```declarative
mvn spring-boot:run
```

Then in Intellij's RUn Remove JVM Debug.

## Packaging as executable jar
```declarative
mvn clean package
```

Runninng
```declarative
java -jar target/hanaeco-integration-test-tool-0.1.0-SNAPSHOT.jar
```
