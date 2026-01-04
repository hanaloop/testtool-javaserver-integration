# Hanaeco Integration Test (Java/SpringBoot)

Hanaeco API를 사용하는 Java Spring Boot 샘플 애플리케이션입니다.  
`org.openapitools`의 [`openapi-generator-maven-plugin`](https://github.com/OpenAPITools/openapi-generator/blob/master/modules/openapi-generator-maven-plugin/README.md)을 이용해 OpenAPI 스펙으로부터 생성된 Java 코드를 기반으로 합니다.

# 애플리케이션 개요

애플리케이션을 실행해 API 키를 입력하면 다음 기능을 이용할 수 있습니다.
- 등록된 기업 조회
- 특정 기업의 제품 조회
- 특정 제품의 배출계수(고유내재배출량) 조회

Hanaeco OpenAPI 스펙 파일 위치:
```declarative
src/main/resources/hanaeco-api-spec-1.8.yaml
```
원본은 Hanaeco 실행 환경의 `http://localhost:9080/apidoc-yaml`에서 가져왔습니다.  
바로 테스트할 수 있는 OpenAPI 페이지는 [http://localhost:9080/apidoc](http://localhost:9080/apidoc)에서 확인합니다.


## 빌드

OpenAPI로부터 Java 클라이언트 코드를 생성하고 컴파일합니다.

```sh
mvn clean compile
```

> 참고: 캐시 문제로 실패할 수 있으니, 실패 시 한 번 더 시도하세요.


## 실행 가능한 JAR 패키징

`pom.xml`에 정의된 `{artifactId}-{version}` 이름으로 `/target` 폴더에 실행 가능한 JAR가 생성됩니다.

```sh
mvn clean package
```

예시: `target/hanaeco-integration-test-tool-0.2.0-SNAPSHOT.jar`

## 애플리케이션 실행

생성된 JAR를 Java 17 이상 JVM으로 실행합니다.

```sh
java -jar target/hanaeco-integration-test-tool-0.2.0-SNAPSHOT.jar
```

애플리케이션이 가동되면 브라우저에서 `http://localhost:8080/`을 엽니다.  
Hanaeco 시스템(백엔드와 웹 앱)이 실행 중이라면 다음을 입력하세요.
- Hanaeco Base URL: `http://localhost:9080`
- API Key: 사용자 [프로필](http://localhost:3000/user/profile)에 표시된 키

"Save configuration"을 누른 뒤 "Open Organization Lookup", "Product Lookup", "Product emission factors" 메뉴로 이동해 데이터를 조회합니다.

## 디버그 실행(IntelliJ Community Edition)

디버그가 필요하면 애플리케이션을 별도 프로세스로 실행한 뒤 IntelliJ에서 원격으로 붙습니다.

터미널에서 실행:
```declarative
mvn spring-boot:run
```

이후 IntelliJ의 Remote JVM Debug 설정으로 연결합니다.
