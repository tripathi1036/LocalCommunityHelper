# Local Helper Backend

A comprehensive Helper Community platform backend built with Spring Boot, providing REST APIs for connecting users with local service providers.

## Overview

Local Helper is a service marketplace platform that enables users to find and book local service providers (helpers) for various tasks. The system supports three main user roles: Users, Helpers, and Admins, each with specific functionalities and permissions.

## Features

### Core Functionality
- **User Management**: Registration, authentication, profile management
- **Helper Registration**: KYC verification, profile setup, availability management
- **Service Requests**: Create, manage, and track service bookings
- **Payment Processing**: Secure payment handling with multiple payment methods
- **Review System**: Rate and review completed services
- **Complaint Management**: Submit and track complaints with admin resolution

### Key Components
- **JWT Authentication**: Secure token-based authentication
- **Role-based Authorization**: ADMIN, USER, HELPER roles with appropriate permissions
- **RESTful APIs**: Comprehensive REST endpoints for all functionalities
- **Database Integration**: PostgreSQL with JPA/Hibernate
- **API Documentation**: Interactive Swagger UI
- **Exception Handling**: Global error handling with custom business exceptions

## Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 21
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA / Hibernate
- **Security**: Spring Security with JWT
- **Documentation**: SpringDoc OpenAPI 3 (Swagger)
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito

## Architecture

### 3-Tier Architecture
```
Controller Layer (REST APIs)
    ↓
Service Layer (Business Logic)
    ↓
Repository Layer (Data Access)
```

### Package Structure
```
com.localhelper/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── dto/            # Data Transfer Objects
│   ├── request/    # Request DTOs
│   └── response/   # Response DTOs
├── entity/         # JPA entities
├── exception/      # Custom exceptions and handlers
├── repository/     # Spring Data repositories
├── security/       # Security configuration and JWT
├── service/        # Business logic services
└── util/           # Utility classes
```

## Getting Started

### Prerequisites
- Java 21 or higher
- Maven 3.8+
- PostgreSQL 12+

### Database Setup
1. Create a PostgreSQL database:
```sql
CREATE DATABASE localhelper_dev;
CREATE USER localhelper_user WITH PASSWORD 'localhelper_password';
GRANT ALL PRIVILEGES ON DATABASE localhelper_dev TO localhelper_user;
```

### Installation & Running
1. Clone the repository
2. Configure database connection in `application-dev.yml`
3. Build and run the application:
```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### API Documentation
Access the interactive API documentation at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `POST /api/auth/register-admin` - Admin registration

### User Operations
- `GET /api/user/profile` - Get user profile
- `PUT /api/user/profile` - Update user profile
- `GET /api/user/helpers` - Search available helpers
- `POST /api/user/service-requests` - Create service request
- `POST /api/user/payments` - Create payment
- `POST /api/user/reviews` - Create review
- `POST /api/user/complaints` - File complaint

### Helper Operations
- `POST /api/helper/register` - Register as helper
- `GET /api/helper/profile` - Get helper profile
- `PUT /api/helper/availability` - Update availability
- `POST /api/helper/service-requests/{id}/accept` - Accept service request
- `GET /api/helper/earnings` - View earnings
- `GET /api/helper/reviews` - View reviews

### Admin Operations
- `GET /api/admin/users` - Manage users
- `GET /api/admin/helpers` - Manage helpers
- `POST /api/admin/helpers/{id}/approve` - Approve helper
- `GET /api/admin/complaints` - Handle complaints
- `GET /api/admin/analytics/dashboard` - View analytics

## User Roles & Permissions

### USER Role
- Register and manage profile
- Search and book helpers
- Make payments and submit reviews
- File complaints

### HELPER Role
- All USER permissions
- Register as service provider with KYC
- Manage availability and service requests
- View earnings and customer reviews
- Accept/reject service requests

### ADMIN Role
- All system permissions
- Approve/reject helper registrations
- Manage users and helpers
- Handle complaints and disputes
- View system analytics and reports

## Configuration

### Environment Profiles
- `dev`: Development environment with detailed logging
- `prod`: Production environment with optimized settings

### Key Configuration Properties
```yaml
app:
  jwtSecret: your-secret-key
  jwtExpirationInMs: 86400000  # 24 hours

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/localhelper_dev
    username: localhelper_user
    password: localhelper_password
```

## Testing

Run tests with:
```bash
mvn test
```

The project includes:
- Unit tests with Mockito
- Integration tests for API endpoints
- Test configuration for different environments

## Deployment

### Docker Support
The application can be containerized using the provided Dockerfile:
```bash
docker build -t local-helper-backend .
docker run -p 8080:8080 local-helper-backend
```

### Production Deployment
1. Set environment variables for database and JWT secret
2. Use production profile: `--spring.profiles.active=prod`
3. Configure external PostgreSQL database
4. Set up SSL/TLS for HTTPS
5. Configure load balancer if needed

## Security Features

- JWT-based stateless authentication
- Password encryption using BCrypt
- Role-based access control
- CORS configuration for cross-origin requests
- Input validation and sanitization
- SQL injection prevention with JPA

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.

## Support

For support and queries, contact: support@localhelper.com