# Archive

_Acts as a gateway for integrating with Formpipe's proxy, enabling long-term document archiving._

## Getting Started

### Prerequisites

- **Java 21 or higher**
- **Maven**
- **Git**
- **[Dependent services](#dependencies)**

### Installation

1. **Clone the repository:**

   ```bash
   git clone git@github.com:Sundsvallskommun/api-service-archive.git
   ```

2. **Configure the application:**

   Before running the application, you need to set up configuration settings.
   See [Configuration](#Configuration)

   **Note:** Ensure all required configurations are set; otherwise, the application may fail to start.

3. **Ensure dependent services are running:**

   If this microservice depends on other services, make sure they are up and accessible. See [Dependencies](#dependencies) for more details.

4. **Build and run the application:**

     ```bash
     mvn spring-boot:run
     ```

## Dependencies

This microservice depends on the following services:

- **Formpipe Proxy**

  - **Purpose:** API used in front of Formpipe LTA's WCF-based API as a REST API, to enable Java-based services and clients to long-term archive documents.
  - **Additional Information:** Refer to its [product page](https://www.formpipe.com/en/public-sector/software/long-term-archive/) to learn more

Ensure that these services are running and properly configured before starting this microservice.

## API Documentation

Access the API documentation via Swagger UI:

- **Swagger UI:** [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

Alternatively, refer to the `openapi.yml` file located in `src/integration-test/resources` for the OpenAPI specification.

## Usage

### API Endpoints

Refer to the [API Documentation](#api-documentation) for detailed information on available endpoints.

### Example Request

```bash
curl -X POST http://localhost:8080/api/{municipalityId}/archive/byggr
```

## Configuration

Configuration is crucial for the application to run successfully. Ensure all necessary settings are configured in `application.yml`.

### Key Configuration Parameters

- **Server Port:**

  ```yaml
  server:
    port: 8080
  ```

- **External Service URLs:**

  ```yaml
  byggr:
      submission-agreement-id: some_value
  integration:
      formpipe-proxy:
          base-url: http://your_formpipe_proxy_url

  ```

### Additional Notes

- **Application Profiles:**

  Use Spring profiles (`dev`, `prod`, etc.) to manage different configurations for different environments.

- **Logging Configuration:**

  Adjust logging levels if necessary.

## Contributing

Contributions are welcome! Please see [CONTRIBUTING.md](https://github.com/Sundsvallskommun/.github/blob/main/.github/CONTRIBUTING.md) for guidelines.

## License

This project is licensed under the [MIT License](LICENSE).

## Code status

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-archive&metric=alert_status)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-archive)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-archive&metric=reliability_rating)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-archive)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-archive&metric=security_rating)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-archive)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-archive&metric=sqale_rating)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-archive)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-archive&metric=vulnerabilities)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-archive)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-archive&metric=bugs)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-archive)ummary/overall?id=Sundsvallskommun_YOUR-PROJECT-ID)

---

© 2024 Sundsvalls kommun
