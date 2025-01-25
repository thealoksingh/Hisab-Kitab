# Hisab-Kitab

Hisab - Kitab is a simple and effective tool for managing personal and group expenses. It’s designed to be user-friendly while ensuring transparency, accountability, and ease of use.

## Table of Contents

- [Features](#features)
- [Backend Details](#backend-details)
- [Technologies Used](#technologies-used)
- [How to run the backend](#how-to-run-the-backend)

## Features

- User Authentication: Secure login and signup with JWT.
- Friend Requests: Send, accept, or reject requests using phone numbers.
- Transaction Management: Track balances, add comments and report transactions.
- Support Tickets: Raise tickets for help and monitor their status.
- Real-Time Notifications: Receive email alerts for critical updates.

## Backend Details

The backend is built with Spring Boot. It utilizes a layered architecture of Controllers, Services, Repositories, DTOs, and Entities. It also follows a modular design for maintainability and scalability.

## Technologies Used

- Language: Java
- Framework: Spring Boot
- Database: MySQL
- Authentication: JWT (JSON Web Token)
- Build Tool: Maven
- Deployment: Docker

### Key Components

- Controllers: Manage HTTP requests and map them to appropriate services.
- Services: Contain the business logic of the application.
- Repositories: Provide data access methods using JPA.
- Entities: Represent database tables.
- DTOs: Facilitate data transfer between layers.

### Configuration Files

- application.properties: Contains database configurations and application settings.

## How to run the backend

1. Clone the repository:
    `https://github.com/thealoksingh/Hisab-Kitab/`
2. Navigate to the project directory:
    `cd Hisab-Kitab`
3. Configure the `application.properties` file:
 ```
    spring.datasource.url=jdbc:mysql://localhost:3306/hisab_kitab_db
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    jwt.secret=your_secret_key
``` 
4. Build and run the project: `mvn spring-boot:run`
   