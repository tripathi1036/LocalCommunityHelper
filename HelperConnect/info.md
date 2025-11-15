# Local Helper Backend

## Overview

Local Helper is a service marketplace platform that connects users with local service providers (helpers). The backend is built using Spring Boot and follows a clean, layered architecture with proper separation of concerns. The system manages user accounts, service requests, payments, reviews, and complaints through a comprehensive REST API.

The platform supports different user roles (users, helpers, admins) with appropriate authorization mechanisms and provides a complete workflow from service discovery to payment processing and feedback collection.

## User Preferences

Preferred communication style: Simple, everyday language.

## System Architecture

### Backend Framework
- **Spring Boot**: Core framework providing dependency injection, auto-configuration, and production-ready features
- **Spring Security**: Handles authentication and authorization with JWT token-based security
- **Spring Data JPA**: Provides repository pattern implementation for database operations
- **Layered Architecture**: Clear separation between controllers, services, repositories, and entities

### API Design
- **RESTful Controllers**: Organized by domain (User, Helper, Admin, Auth) for clear API structure
- **DTO Pattern**: Separate request/response DTOs to decouple API contracts from internal entities
- **Global Exception Handling**: Centralized error handling with custom business exceptions
- **API Documentation**: Swagger/OpenAPI integration for interactive API documentation

### Data Architecture
- **JPA Entities**: Object-relational mapping for core business entities (User, Helper, ServiceRequest, Payment, Review, Complaint)
- **Repository Pattern**: Spring Data JPA repositories for data access abstraction
- **Database Configuration**: Centralized database configuration management

### Security Architecture
- **JWT Authentication**: Stateless token-based authentication system
- **Role-based Authorization**: Support for different user roles with appropriate permissions
- **Custom Security Filter**: JWT authentication filter for request processing
- **User Details Service**: Custom implementation for loading user authentication details

### Deployment Architecture
- **Docker Support**: Containerized deployment with Dockerfile and docker-compose
- **Build Automation**: Maven/Gradle build configuration with dependency management
- **CI/CD Scripts**: Deployment and database migration scripts for automated workflows

## External Dependencies

### Core Framework Dependencies
- **Spring Boot Starter Web**: RESTful web services and embedded server
- **Spring Boot Starter Security**: Authentication and authorization framework
- **Spring Boot Starter Data JPA**: Database persistence and ORM capabilities
- **SpringDoc OpenAPI**: API documentation generation and Swagger UI

### Database Integration
- **JPA/Hibernate**: Object-relational mapping and database abstraction
- **Database Driver**: JDBC driver for chosen database system (configuration-dependent)

### Authentication & Security
- **JWT Library**: JSON Web Token implementation for stateless authentication
- **Spring Security**: Core security framework for authentication and authorization

### Development & Documentation
- **Swagger/OpenAPI**: Interactive API documentation and testing interface
- **Docker**: Containerization platform for deployment consistency

### Build & Deployment
- **Maven/Gradle**: Build automation and dependency management
- **Docker Compose**: Multi-container application orchestration