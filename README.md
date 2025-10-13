#Product Order Service
https://github.com/tzzlabs/product-order-service/actions/workflows/maven.yml/badge.svg

A robust Spring Boot microservice for managing product orders with CRUD operations, authentication, order tracking, and cloud integrations.

🚀 Features
Complete order management with CRUD operations

JWT Authentication & Authorization

Order status tracking

AWS S3 integration for invoice/document storage

Asynchronous order notifications via AWS SQS

Real-time event logging with AWS DynamoDB

Swagger API documentation

CI/CD with GitHub Actions (current) and Jenkins (planned)

🛠️ Tech Stack
Backend: Java 17, Spring Boot 3.5, Spring Security

Database: PostgreSQL (primary), AWS DynamoDB (events)

Cloud Services: AWS S3, SQS, DynamoDB, EC2

Infrastructure: Docker (planned), Jenkins (planned), Swagger/OpenAPI

⚡ Quickstart (Local Development)
Copy configuration template:

bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
Update application.properties with your credentials:

properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/order_db
spring.datasource.username=your_username
spring.datasource.password=your_password

# AWS Configuration
cloud.aws.credentials.access-key=your-aws-access-key
cloud.aws.credentials.secret-key=your-aws-secret-key
cloud.aws.region.static=us-east-1
cloud.aws.s3.bucket-name=your-invoice-bucket
cloud.aws.sqs.queue-url=your-order-notifications-queue-url
Build and run:

bash
mvn clean install
mvn spring-boot:run
Access the application:

Application: http://localhost:8080

Swagger UI: http://localhost:8080/swagger-ui.html

🚢 Deployment
CI/CD: Automated builds with GitHub Actions

Planned Enhancements:

Jenkins pipelines for advanced CI/CD

Docker containerization

AWS EC2 deployment with environment-specific configurations

📚 API Documentation
Interactive API documentation available at /swagger-ui.html when the application is running.
