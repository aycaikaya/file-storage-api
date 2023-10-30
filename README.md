# file-storage-api
File Storage API for Java Developer Challenge

## Overview
[This Spring Boot project implements a File Storage and Listing API, providing a secure and efficient way to store, manage, and retrieve files on a server. It offers the following key features:

File Storage: The API allows users to upload files to the server via a REST endpoint. The file name, extension, and size are obtained through the API and stored on the server's file system.

Database Integration: In addition to saving files on the file system, the API maintains metadata about the uploaded files in a relational database. This information includes the file's path, name, extension, and size.

File Validation: The API enforces strict validation rules. It checks that file sizes do not exceed 5MB and that file extensions are limited to png, jpeg, jpg, docx, pdf, and xlsx. If these rules are violated, the API prevents file storage and returns a system error message.

File Retrieval: Users can retrieve information about stored files via a REST endpoint, providing details about each file.

Content Retrieval: The API allows users to retrieve the content of stored files as a byte array via a REST endpoint, making it easy to access and manipulate the actual file data.

File Management: Files can be updated and deleted through REST endpoints, providing full control over stored content.

Security: The API is secured with JSON Web Tokens (JWT), ensuring that only authorized users can access its features.

Documentation: Comprehensive API documentation is available via Swagger, making it easy for users and developers to understand and interact with the endpoints.]

## Prequisites
Java version: 17


## How to Run
To use this API, follow these steps:

  1) Clone the repository to your local machine.
     git clone git@github.com:aycaikaya/file-storage-api.git

  3) Configure the project's application properties, specifying the database connection and security settings.

  4) Build and run the Spring Boot application.

  5) Explore the API via the provided Swagger documentation at http://localhost:8080/swagger-ui.html#/
     (assuming the application is running locally on port 8080).

Begin uploading, managing, and retrieving files securely.

## Author
Ayça İdil Kaya

## License
This project is licensed under the MIT License - see the LICENSE file for details.

## Notes
Github link to algorithm-study: https://github.com/aycaikaya/algorithm-study





