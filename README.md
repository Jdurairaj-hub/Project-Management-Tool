# Project Management Tool

This project has been created as a requirement for the release-2 deliverable in the CSCI 3130 course

## Prerequisites

Before running the application, ensure you have the following software installed:

- React.js and npm (version 18.2.0)
- Java Development Kit (JDK) (version 17)

## Installation Steps

1. Clone the repository:

## Instructions for Front-end (React) 

1. Use npm start
2. Frontend Setup:
- Change to the frontend directory:
  ```
  cd frontend

  cd trello-frontend
  ```
- Install dependencies: 
  ```
  npm install
  npm install axios
  npm install redux-thunk
  npm install sass-loader node-sass resolve-url-loader --save-dev
  ```
  (To avoid facing any issues, can run the command - npm install @mui/lab as well, as sometimes @mui/lab does not get installed automatically.)
## Backend Setup:

- Connect to Dal VPN
- Load MYSQL Server, and login using the csci3130_group22 credentials
- Install these dependencies (through pom.xml file):
  - spring-boot-starter-security - Version 3.1.1
  - spring-boot-starter-thymeleaf - Version 3.1.1
  - spring-boot-starter-data-jpa
  - spring-boot-devtools - Version 3.1.1
  - hibernate-validator - Version 7.0.1.Final
  - jakarta.validation-api - Version 3.0.2
  - spring-boot-starter-web
  - mysql-connector-j - Scope runtime
  - spring-boot-starter-test - Scope test
  

- Change to the backend directory:
  ```
  cd backend
  ```
- Build the backend: 
  ```
  mvn clean install
  ```

## Configuration

Before running the application, configure the following:

- Set the environment variables:
- VARIABLE_NAME_1: Description of the variable
- VARIABLE_NAME_2: Description of the variable

- Modify the application properties:
- Open `backend/src/main/resources/application.properties`.
- Update the configuration values as needed.

## Running the Application

1. Frontend:
- Start the frontend development server:
  ```
  npm start
  ```
- The frontend can be accessed at: [http://localhost:3000](http://localhost:3000)

2. Backend:
- Run the Spring Boot application:
  ```
  mvn spring-boot:run
  ```

## Additional Notes

- USER API Endpoints:
- `/api/user/signUp`: Signing up user for the first time.
- `/api/user/logIn`: Logging in user.
- `/api/user/logOut`: Log Out the user.
- `/api/user/{userId}/tasks/{taskId}`: Assign task to a user.
- `/api/user/passwordReset`: Reset user's password.
- `/api/user/{userId}/activities/{activityId}`: Add user to an activity.
- `/api/user/useraddUserToOrganization`: Add user to an organization.
- `/api/user/{userId}`: Update user's information

- Board API Endpoints:
- `/api/board/create`: Creating a board for the first time.
- `/api/board/{boardId}`: Updating board details.
- `/api/board/{workspaceId}/{boardId}`: Creating a board in a workspace.
- `/api/board/{boardId}/delete`: Delete a board from a workspace.
- `/api/board/boards`: Retrieve all the boards.

- Organization API Endpoints:
- `/api/organization/create`: Creating an organization for the first time.
- `/api/organization/{organizationId}`: Updating organization details.

- Organization API Endpoints:
- `/api/organization/create`: Creating an organization for the first time.
- `/api/organization/{organizationId}`: Updating organization details.

- Task API Endpoints:
- `/api/task/create`: Creating a task for the first time.
- `/api/task/{taskId}`: Updating task details.

- Workspace API Endpoints:
- `/api/workspace/create`: Creating a task for the first time.
- `/api/workspace/{workspaceId}`: Updating workspace details.
- `/api/workspace/{workspaceId}/addMember/{userEmail}`: Updating workspace details.
- `/api/workspace/{workspaceId}/removeMember/{userEmail}`: Removing a user from a workspace.
- `/api/workspace/workspaces`: Returns all the workspaces.

- File Structure:
- `/frontend`: Contains the React frontend files.
- `/backend`: Contains the Spring Boot backend files.

- Troubleshooting:
- If you encounter any issues, try restarting the server or clearing the cache.
