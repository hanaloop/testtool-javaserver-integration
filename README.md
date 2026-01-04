# Hanaeco Integration Test (Java/SpringBoot)

This is a sample Java Spring Boot application that consumes Hanaeco's API for demo purposes.

It uses the [`openapi-generator-maven-plugin`](https://github.com/OpenAPITools/openapi-generator/blob/master/modules/openapi-generator-maven-plugin/README.md) from `org.openapitools` to generate Java code from the OpenAPI specification.

# Application Overview

When an API key is provided, the web application can:
- query registered organizations
- query products for a specified organization
- query emission factors (product footprint) for a specified product


The Hanaeco OpenAPI spec can be found in:
```declarative
src/main/resources/hanaeco-api-spec-1.8.yaml
```

Originally obtained from `http://localhost:9080/apidoc-yaml`

Note: For a human-readable, testable OpenAPI page, open [http://localhost:9080/apidoc](http://localhost:9080/apidoc).


## Building and running the application

Generate the Java client code and compile:

```sh
mvn clean compile
```

> NOTE: In some cases `mvn clean compile` may fail due to cache issues; just try again.


## Packaging as executable jar

Package the app as an executable JAR under `/target` named `{artifactId}-{version}` from `pom.xml`:

```sh
mvn clean package
```

Example output: `target/hanaeco-integration-test-tool-0.2.0-SNAPSHOT.jar`

## Running the application

Run the generated JAR with Java 17 or newer:

```sh
java -jar target/hanaeco-integration-test-tool-0.2.0-SNAPSHOT.jar
```

Open `http://localhost:8080/` in your browser.

Assuming the Hanaeco system (backend server and web application) is up, enter:
- Hanaeco Base URL: `http://localhost:9080`
- API Key: the key shown in the user's [profile](http://localhost:3000/user/profile)

Click "Save configuration", then use "Open Organization Lookup", "Product Lookup", or "Product emission factors".

## Running in debug mode (IntelliJ community edition)

To debug in IntelliJ, start the app in a separate process listening on the debug port:

Run the application from terminal:
```declarative
mvn spring-boot:run
```

Then attach IntelliJ's Remote JVM Debug configuration.
