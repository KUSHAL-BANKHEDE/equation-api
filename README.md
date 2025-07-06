# Equation API

A Spring Boot RESTful application for storing, retrieving, and evaluating algebraic equations using a postfix (expression) tree. Supports in-memory storage and JSON-based requests/responses.

## Features
- Store algebraic equations (infix) and parse to postfix tree
- Retrieve stored equations (reconstructed infix)
- Evaluate equations with variable substitution
- In-memory storage, no database required
- Comprehensive unit and integration tests

## Setup Instructions

### Prerequisites
- Java 17+
- Gradle (or use the included wrapper)

### Build & Run

```
cd equation-api
./gradlew bootRun
```

The API will be available at `http://localhost:8080/api/equations`.

## API Endpoints

### 1. Store an Algebraic Equation
- **URL:** `/api/equations/store`
- **Method:** `POST`
- **Request Body:**
  ```json
  { "equation": "3*x + 2*y - z" }
  ```
- **Response:**
  ```json
  { "message": "Equation stored successfully", "equationId": "1" }
  ```

### 2. Retrieve Stored Equations
- **URL:** `/api/equations`
- **Method:** `GET`
- **Response:**
  ```json
  {
    "equations": [
      { "equationId": "1", "equation": "3*x + 2*y - z" },
      { "equationId": "2", "equation": "x^2 + y^2 - 4" }
    ]
  }
  ```

### 3. Evaluate an Equation
- **URL:** `/api/equations/{equationId}/evaluate`
- **Method:** `POST`
- **Request Body:**
  ```json
  { "variables": { "x": 2, "y": 3, "z": 1 } }
  ```
- **Response:**
  ```json
  {
    "equationId": "1",
    "equation": "3*x + 2*y - z",
    "variables": { "x": 2, "y": 3, "z": 1 },
    "result": 10
  }
  ```

## Error Handling
- Returns `400 Bad Request` with an error message for invalid input or missing variables.

## Testing

### Run All Tests
```
./gradlew test
```

- Unit and integration tests cover storing, retrieving, evaluating, and error scenarios.

## Notes
- No database required; all data is in-memory.
- Testable via Postman or any HTTP client.
- No UI provided (API only). 
