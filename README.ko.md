# Hanaeco Integration Test (Java/SpringBoot)

Hanaeco API를 활용하는 Java Spring Boot 샘플 애플리케이션입니다.

본 샘플은 `org.openapitools`의 [`openapi-generator-maven-plugin`](https://github.com/OpenAPITools/openapi-generator/blob/master/modules/openapi-generator-maven-plugin/README.md)을 사용해 OpenAPI 스펙에서 생성한 Java 코드를 활용하여 개발되었습니다.

# 애플리케이션 개요

어플리케이션 실행 후 API 키를 입력하면 다음을 기능을 수행할 수 있습니다.
- 등록된 기업 조회
- 특정 기업의 제품 조회
- 특정 제품의 배출계수(고유내재배출량) 조회

Hanaeco OpenAPI 스펙은 다음 경로에 있습니다.
```declarative
src/main/resources/hanaeco-api-spec-1.8.yaml
```

원본 스펙은 Hanaeco 실행에서 `http://localhost:9080/apidoc-yaml`에서 가져왔습니다.

읽기 쉽고 바로 테스트할 수 있는 OpenAPI 페이지는 [http://localhost:9080/apidoc](http://localhost:9080/apidoc)에서 확인할 수 있습니다.


## 빌드 및 실행

OpenAPI로부터 Java 클라이언트 코드를 생성한 뒤 컴파일

```sh
mvn clean compile
```

> 참고: 캐시 문제로 `mvn clean compile`이 실패할 수 있습니다. 그럴 때 재시도 하시면 됩니다.


## 실행 가능한 JAR 패키징

다음 커맨드로 `pom.xml`에 정의된 `{artifactId}-{version}` 이름으로 `/target` 폴더에 실행 가능한 JAR를 만듭니다.

```sh
mvn clean package
```

생성된 Jar 파일 예: `target/hanaeco-integration-test-tool-0.2.0-SNAPSHOT.jar`

## 애플리케이션 실행

Jar파일이 생성되면 Java 17 이상 JVM으로 실행이 가능합니다.

```sh
java -jar target/hanaeco-integration-test-tool-0.2.0-SNAPSHOT.jar
```

어플레케이션이 가동되면 브라우저에서 `http://localhost:8080/`을 엽니다.

Hanaeco 시스템(백엔드와 웹 앱)이 실행 중이라면 다음을 입력하세요.
- Hanaeco Base URL: `http://localhost:9080`
- API Key: 사용자 [프로필](http://localhost:3000/user/profile)에 표시된 키

"Save configuration"을 누른 뒤 "Open Organization Lookup", "Product Lookup", "Product emission factors" 메뉴로 이동해 각 데이터를 조회할 수 있습니다.

## 디버그 실행(IntelliJ Community Edition)

디버그가 필요하면 애플리케이션을 별도 프로세스로 실행하고 디버그 포트를 열어둡니다.

터미널에서 실행:
```declarative
mvn spring-boot:run
```

그런 다음 IntelliJ의 Remote JVM Debug 설정으로 연결합니다.
