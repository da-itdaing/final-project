# Getting Started

> Quick reference for common build, test, and documentation tasks.

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.6/gradle-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.6/gradle-plugin/packaging-oci-image.html)
* [Spring Boot Testcontainers support](https://docs.spring.io/spring-boot/3.5.6/reference/testing/testcontainers.html#testing.testcontainers)
* [Testcontainers MySQL Module Reference Guide](https://java.testcontainers.org/modules/databases/mysql/)
* [Spring Web](https://docs.spring.io/spring-boot/3.5.6/reference/web/servlet.html)
* [Validation](https://docs.spring.io/spring-boot/3.5.6/reference/io/validation.html)
* [Spring Security](https://docs.spring.io/spring-boot/3.5.6/reference/web/spring-security.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/3.5.6/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Flyway Migration](https://docs.spring.io/spring-boot/3.5.6/how-to/data-initialization.html#howto.data-initialization.migration-tool.flyway)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/3.5.6/reference/actuator/index.html)
* [Testcontainers](https://java.testcontainers.org/)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/3.5.6/reference/using/devtools.html)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/3.5.6/specification/configuration-metadata/annotation-processor.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

### Testcontainers support

This project uses [Testcontainers at development time](https://docs.spring.io/spring-boot/3.5.6/reference/features/dev-services.html#features.dev-services.testcontainers).

Testcontainers has been configured to use the following Docker images:

* [`mysql:latest`](https://hub.docker.com/_/mysql)

### OpenAPI Generation (CI & Local)

CI uses the Gradle task `generateOpenApiDocs` (profile `openapi`) to produce `build/openapi/openapi.yaml` which is published to GitHub Pages. Run locally:

```bash
./gradlew generateOpenApiDocs
open build/openapi/openapi.yaml
```

### Common Gradle Commands

```bash
./gradlew clean build         # Full build + tests
./gradlew test                # Run tests only
./gradlew testMaster          # Domain-specific test task example
./gradlew bootRun             # Run with default profile (local)
./gradlew generateOpenApiDocs # Produce OpenAPI spec
```

### Troubleshooting Quick Notes

| Symptom | Likely Cause | Fix |
|---------|--------------|-----|
| Wrapper validation fails | Corrupted gradle-wrapper.jar | Re-create wrapper (`./gradlew wrapper --gradle-version <ver>`) |
| OpenAPI task timeout | App cold start / heavy migrations | Increase `waitTimeInSeconds` in `openApi {}` block |
| H2 schema errors | Flyway enabled on openapi profile | Ensure `flyway.enabled=false` in `application-openapi.yml` |
| 401 on public endpoint | Security config regression | Verify `SecurityConfig` permitAll matchers |

### Security Reminder

Never commit production secrets. Use environment variables or GitHub Actions encrypted secrets.


Please review the tags of the used images and set them to the same as you're running in production.

