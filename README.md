
# Integration Final Project

## Project Description

This project is a Spring Boot application that integrates with both SOAP and REST services to process input data. The application validates the input (mobile, SMS, and national ID), makes calls to mock SOAP and REST services, and returns a final response after processing.

The project also includes error handling, ensuring proper validation and processing of inputs, and meaningful responses for exceptions. Swagger is used to document the API.

## Features

- **Input Validation**: Validates mobile number, SMS length, and national ID.
- **SOAP Service Integration**: Sends requests to a SOAP service and processes the response.
- **REST Service Integration**: Depending on the SOAP response, makes additional REST service calls.
- **Custom Exception Handling**: Handles custom exceptions like invalid mobile numbers or failed SOAP requests.
- **Swagger Integration**: API documentation is generated using Swagger.
- **Multi-SMS Handling**: Based on SMS length, the system calculates if the message will be sent as one or more SMS messages.

## Technologies Used

- Java
- Spring Boot
- SOAP UI
- Postman
- REST API
- SOAP API
- Swagger (OpenAPI)
- Maven
- Lombok

## Getting Started

### Prerequisites

- Java 22+
- Maven
- Spring Boot
- A SOAP mock service running on `http://localhost:8088/s`
- A REST mock service running on `http://localhost:8080/rest`

### Installation

1. Clone the repository to your local machine:
    ```bash
    git clone https://github.com/abdulrahmankafafy/IntegrationTask
    ```

2. Navigate to the project directory:
    ```bash
    cd integrationFinal
    ```

3. Build the project using Maven:
    ```bash
    mvn clean install
    ```

4. Run the Spring Boot application:
    ```bash
    mvn spring-boot:run
    ```

### Endpoints

- **POST** `/api/processRequest`: Processes input data by calling SOAP and REST services.  
  **Request Body**:  
  ```json
  {
      "mobile": "12345678901",
      "sms": "Hello, World!",
      "nid": "12345678901234"
  }
  ```  
  **Response Body**:  
  ```json
  {
      "statusDesc": "Mobile numbers match.",
      "code": "200",
      "message": "Request processed successfully",
      "cifNumber": "ABC123",
      "mobileWithoutPrefix": "12345678901",
      "smsMessage": "this message will be sent as 1 sms"
  }
  ```

### Swagger Documentation

To access the API documentation, go to `http://localhost:8080/swagger-ui.html` in your browser after running the application.

## Error Handling

- **SpecificException**: Handles validation errors and service failures.
- **GenericException**: Handles unexpected errors.

### Example Error Response:
```json
{
    "statusDesc": "Failed",
    "code": "400",
    "message": "Invalid mobile number. The number should be 11 digits."
}
```

