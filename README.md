# Hisab-Kitab

Hisab - Kitab is a simple and effective tool for managing personal and group expenses. It’s designed to be user-friendly while ensuring transparency, accountability, and ease of use.

## Table of Contents

- [Features](#features)
- [Backend Details](#backend-details)
- [Technologies Used](#technologies-used)
- [How to run the backend](#how-to-run-the-backend)
- [Members](#members)
- [Rules for contribution](#rules-for-contribution)


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

## How to run the backend

1. Clone the repository:
    `https://github.com/thealoksingh/Hisab-Kitab/`
2. Navigate to the project directory:
    `cd Hisab-Kitab`
3. Configure the environment variables:
   - Open the Environment Variables settings on your system.
   - In the **System Variables** section, click on **New** and add the following variables:
     
     | Variable Name                 | Variable Values                          |
     |-------------------------------|------------------------------------------|
     | `SPRING_DATASOURCE_URL`       | `jdbc:mysql://localhost:3306/hisabkitab` |
     | `SPRING_DATASOURCE_USERNAME`  | `your_username`                          |
     | `SPRING_DATASOURCE_PASSWORD`  | `your_password`                          |
   
   - Click **OK** on all dialog boxes to save the changes.
4. Reopen Eclipse IDE to integrate the changes.
5. Run the main file as a Java application to start the backend server.

## Members

- Alok Kumar
- Jay Singh
- Ravi Kumar
- Anukrity Srivastava
  
## Rules for contribution

We welcome contributions from everyone! To ensure smooth collaboration, please follow these guidelines:

1. **Fork the Repository**: Start by forking the project to your own GitHub account.
2. **Create a Feature Branch**: Make your changes in a separate branch named descriptively, like feature/add-user-logging or bugfix/fix-login-issue.
3. **Follow Code Standards**: Write clean, readable, and well-documented code. Follow the existing coding style of the project.
4. **Commit Messages**: Write meaningful and concise commit messages that explain the changes you've made.
5. **Pull Request (PR)**:
    - Ensure your code is thoroughly tested before creating a PR.
    - Link the relevant issue (if applicable) in your PR description.
    - Include a short summary of your changes and their purpose.
6. **Review Process**: Be open to feedback from maintainers and other contributors. Update your PR as needed based on review comments.
7. **Respect and Collaboration**: Please be respectful in discussions and maintain a collaborative attitude.

Note: Always pull the latest changes from the main branch before starting new work to avoid merge conflicts.
   
