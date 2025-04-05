# Project 4 Task 2 Writeup

## Student Information
## Jung Ho Park, junghop

## 1. Native Android Application

### Description
The Android application allows users to search for books by entering a title or author name. It displays search results in a list view, showing the book title, author, and publication year.

### Screenshots
[Include screenshots of your Android app here]

### Implementation Details
- Uses three different kinds of Views:
  1. EditText - For user input of search terms
  2. Button - For initiating the search
  3. ListView - For displaying the search results

- The application makes HTTP requests to the web service using the AsyncTask class to ensure that network operations are performed on a background thread.

- The app parses the JSON response and displays formatted book information to the user.

## 2. Web Service Implementation

### Description
The web service is built using Java Servlets and handles requests from the Android application. It fetches data from the Open Library API, processes it, and returns a simplified JSON response to the Android app.

### API Details
- **Endpoint:** `/api/books`
- **Method:** GET
- **Parameters:** 
  - `q`: The search query (required)
  - `limit`: Maximum number of results to return (optional, default: 5)

### Business Logic
The web service:
1. Receives the search query from the Android app
2. Makes a request to the Open Library API
3. Processes the response to extract relevant information
4. Formats the data as a simpler JSON response
5. Logs the interaction details to MongoDB Atlas
6. Returns the formatted response to the Android app

### Screenshots
[Include screenshots of your web service API responses here]

## 3. MongoDB Database Integration

### Description
The application uses MongoDB Atlas to store log data for each interaction with the web service. The logs include information about the request, API performance, and response details.

### Data Model
Log entries include:
- Timestamp
- Search query
- Client IP address
- User agent (device information)
- API request duration
- Number of results found
- Response size

### Screenshots
[Include screenshots of your MongoDB collections/data here]

## 6. Dashboard Implementation

### Description
The dashboard provides real-time analytics and log information about the application usage. It displays three key analytics and a full log listing.

### Analytics
The dashboard shows:
1. Top search queries by frequency
2. Average API response times by hour
3. Top user agents (device types) making requests

### Log Display
The logs are displayed in a formatted table showing:
- Timestamp
- Search query
- Client IP
- Number of results
- API request duration

### Screenshots
[Include screenshots of your dashboard here]

## 7. GitHub Codespaces Deployment

### Description
The web service is deployed to GitHub Codespaces, making it accessible from the Android application.

### Setup Steps
1. Created a GitHub repository with the provided template
2. Built the web application as ROOT.war
3. Deployed to Codespaces using the provided Dockerfile
4. Set port visibility to Public

### Screenshots
[Include screenshots of your Codespaces deployment here]

## Error Handling

The application handles the following error conditions:

### Invalid Mobile App Input
- The Android app validates that the search field is not empty
- Provides a toast message if the user attempts to search with an empty query

### Invalid Server-Side Input
- The web service validates all incoming parameters
- Returns appropriate error messages and status codes for invalid inputs

### Network Failure
- The Android app displays error messages if it cannot connect to the web service
- The web service logs connection failures to the third-party API

### Third-Party API Unavailability
- The web service gracefully handles failures when the Open Library API is unavailable
- Returns a user-friendly error message to the Android app

### Invalid Third-Party API Data
- The web service validates the structure of the data returned by the Open Library API
- Handles missing fields or unexpected data formats