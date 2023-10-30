# file-storage-api
Java Developer Challenge

Table of Contents
1) Overview
2) Prerequisites
3) Getting Started
4) Project Structure
5) API Documentation
6) Authentication
7) Tests
9) Contributing
10) License
11) Author
12) Notes


OVERVIEW

This Spring Boot project implements a File Storage and Listing API, providing a secure and efficient way to store, manage, and retrieve files on a server. It offers the following key features:

File Storage: 
The API allows users to upload files to the server via a REST endpoint. The file name, extension, and size are obtained through the API and stored on the server's file system.

Database Integration:
In addition to saving files on the file system, the API maintains metadata about the uploaded files in a relational database. This information includes the file's path, name, extension, and size.

File Validation:
The API enforces strict validation rules. It checks that file sizes do not exceed 5MB and that file extensions are limited to png, jpeg, jpg, docx, pdf, and xlsx. If these rules are violated, the API prevents file storage and returns a system error message.

File Retrieval:
Users can retrieve information about stored files via a REST endpoint, providing details about each file.

Content Retrieval:
The API allows users to retrieve the content of stored files as a byte array via a REST endpoint, making it easy to access and manipulate the actual file data.

File Management: Files can be updated and deleted through REST endpoints, providing full control over stored content.

Security: 
The API is secured with JSON Web Tokens (JWT), ensuring that only authorized users can access its features.

Documentation:
Comprehensive API documentation is available via Swagger, making it easy for users and developers to understand and interact with the endpoints.


PREREQUISITES
java version: 11


GETTING STARTED
bash
Copy code
# Clone the repository
git clone https://github.com/yourusername/your-project.git

# Navigate to the project directory
cd file-storage-api

# Build and run the project
mvn spring-boot:run


PROJECT STRUCTURE

src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── java
│   │   │           └── filestorageapi
│   │   │               ├── FileStorageApiApplication.java
│   │   │               ├── config
│   │   │               │   ├── ApplicationConfig.java
│   │   │               │   ├── JwtAuthenticationFilter.java
│   │   │               │   ├── JwtService.java
│   │   │               │   ├── SecurityConfig.java
│   │   │               │   └── SwaggerConfig.java
│   │   │               ├── controller
│   │   │               │   ├── AuthenticationController.java
│   │   │               │   └── FileController.java
│   │   │               ├── model
│   │   │               │   ├── AuthenticationRequest.java
│   │   │               │   ├── AuthenticationResponse.java
│   │   │               │   ├── FileEntity.java
│   │   │               │   ├── RegisterRequest.java
│   │   │               │   ├── Role.java
│   │   │               │   └── User.java
│   │   │               ├── repository
│   │   │               │   ├── FileRepository.java
│   │   │               │   └── UserRepository.java
│   │   │               └── service
│   │   │                   ├── AuthenticationService.java
│   │   │                   └── FileService.java
│   │   └── resources
│   │       ├── application.properties
│   │       ├── static
│   │       │
│   │       │   └── sample-filesea873fa4-41bb-4385-af9c-175d91987f9d.pdf
│   │       └── templates
│   └── test
│       └── java
│           └── com
│               └── java
│                   └── filestorageapi
│                       ├── FileStorageApiApplicationTests.java
│                       ├── controller
│                       │   └── FileControllerTests.java
│                       └── service
│                           └── FileServiceTests.java



API DOCUMENTATION
Swagger UI can be accessed at http://localhost:8080/swagger-ui.html#/

TESTS
bash
Copy code
# Run tests
mvn test

LICENSE
This project is open-source and is licensed under the MIT License. For more details, please refer to the LICENSE file.

AUTHOR
Ayça İdil Kaya

NOTES:
Here is the github repo for algorithm study: https://github.com/aycaikaya/algorithm-study

