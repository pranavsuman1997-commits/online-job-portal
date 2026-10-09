# Online Job Portal

## Project Overview
Online Job Portal is a Java-based desktop application developed to demonstrate the basic working of an online job portal. It allows users to view available job opportunities and apply for jobs through a simple graphical user interface.

## Objectives
- To develop a simple job portal application using Java.
- To display available job opportunities.
- To allow users to apply for jobs.
- To store and retrieve job-related data using MySQL.
- To understand Java database connectivity using JDBC.

## Technologies Used
- **Programming Language:** Java
- **GUI:** Java Swing
- **Database:** MySQL
- **Database Connectivity:** JDBC
- **IDE:** IntelliJ IDEA
- **Version Control:** Git and GitHub

## Features
- User-friendly graphical interface.
- Display of available jobs.
- Job application functionality.
- MySQL database integration.
- Basic demonstration of job portal operations.

## Project Structure
- `DBConnection.java` – Establishes the connection between Java and MySQL.
- `Job.java` – Represents job-related data.
- `JobPortalApp.java` – Contains the main application and graphical interface.
- `database.sql` – Contains the SQL commands required to set up the database.

## Requirements
- Java Development Kit (JDK)
- MySQL Server
- IntelliJ IDEA or another Java IDE
- MySQL Connector/J library

## Database Setup
1. Install and start MySQL Server.
2. Open MySQL Workbench.
3. Open and execute the `database.sql` file.
4. Update the database connection settings in `DBConnection.java` with your local MySQL username, password, and database details.
5. Add the MySQL Connector/J library to the project.

**Security Note:** Do not publish your actual database password or other sensitive credentials on GitHub.

## How to Run the Project
1. Open the project in IntelliJ IDEA.
2. Ensure that the required JDK is configured.
3. Add the MySQL Connector/J library.
4. Configure the database connection.
5. Execute `database.sql` in MySQL Workbench.
6. Run `JobPortalApp.java`.
7. View the available jobs and test the job application functionality.

## Testing
The application was tested locally to verify that the graphical interface opens, job listings are displayed, and the job application functionality works.

## Limitations
- This is a basic academic prototype.
- Advanced features such as user authentication, employer dashboards, resume uploads, and job recommendations are not included unless implemented separately.

## Future Scope
- Add user registration and login.
- Create separate job seeker and employer dashboards.
- Add resume upload functionality.
- Implement job search and filtering.
- Improve security and user experience.

## Project Information
- **Project Name:** Online Job Portal
- **Team Name:** CypherCrew
- **Developed By:** Pranav Suman (ADMIN), Dhruv Golay (Member), Sundram Kumar(Mmeber), Dinesh Yadav (Member)
- **GitHub Repository:** Paste your repository URL here

## Conclusion
The Online Job Portal project demonstrates the use of Java Swing, JDBC, and MySQL to build a basic desktop application for viewing job opportunities and applying for jobs. It provides practical experience in Java application development and database integration.
