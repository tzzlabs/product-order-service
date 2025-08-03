# Interview Talking Points

## What this project demonstrates
- Spring Boot 3.5 with Java 17
- PostgreSQL persistence via Spring Data JPA
- Role-based security with Spring Security (in-memory users for demo)
- OpenAPI / Swagger UI integration (springdoc)
- Proper layering: controller, service, repository
- Error handling & validation (planned next)
- Dockerization & CI/CD design (planned)
- Deployment target: AWS EC2 (design in README)

## Quick demo script
1. Clone repo.
2. Copy \`application.properties.example\` → \`application.properties\`, fill DB creds.
3. Start PostgreSQL and create \`orderdb\`.
4. Build & run: \`./mvnw clean package && java -jar target/...jar\`
5. Open Swagger: \`http://localhost:8080/swagger-ui/index.html\` (auth: user/password)
6. Show creating/fetching an order via Swagger or curl:
   \`\`\`bash
   curl -u user:password http://localhost:8080/api/orders
   \`\`\`

## Next planned enhancements
- DTO + input validation
- Replace in-memory auth with persistent users
- JWT auth
- Docker container + push to Docker Hub
- Jenkins pipeline for CI/CD
- Deploy to EC2 with health checks

