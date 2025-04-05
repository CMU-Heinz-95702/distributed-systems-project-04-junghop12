# Task 2: Book Search Application

## Overview
This application allows users to search for books using the Open Library API. The application consists of three components:

1. **Android Application**: A native Android app that lets users search for books.
2. **Web Service**: A Java servlet-based web service that processes requests from the Android app, communicates with the Open Library API, and logs all interactions.
3. **Dashboard**: A web-based dashboard that displays analytics and logs from the application usage.

## Project Structure

### Web Service
- `src/main/java/edu/cmu/task2/BookSearchService.java`: The main servlet that handles requests from the Android app
- `src/main/java/edu/cmu/task2/DashboardServlet.java`: The servlet that provides the dashboard interface
- `src/main/webapp/WEB-INF/dashboard.jsp`: The JSP page for the dashboard
- `src/main/webapp/index.html`: Simple welcome page for the web service
- `src/main/webapp/WEB-INF/web.xml`: Web application configuration

### Android App
- `BookSearchApp/`: Android Studio project for the mobile application
  - `app/src/main/java/edu/cmu/booksearchapp/MainActivity.java`: Main activity for the Android app
  - `app/src/main/res/layout/activity_main.xml`: Layout for the main activity
  - `app/src/main/AndroidManifest.xml`: Android app manifest file

## Setup Instructions

### 1. MongoDB Atlas Setup
Follow the instructions in MONGODB_SETUP.md to set up your MongoDB Atlas database.

### 2. Deploying the Web Service
1. Update the MongoDB connection string in both `BookSearchService.java` and `DashboardServlet.java`
2. Build the project using Maven:
   ```
   mvn clean package
   ```
3. Deploy the generated ROOT.war file to your GitHub Codespace following the instructions in the project write-up.

### 3. Android App Configuration
1. Open the Android project in Android Studio
2. Update the `WEB_SERVICE_URL` constant in `MainActivity.java` with your deployed Codespace URL
3. Build and run the app on an Android device or emulator

## Using the Application

### Android App
1. Launch the app on your Android device
2. Enter a book title, author, or keyword in the search box
3. Tap "Search" to retrieve results
4. View the list of books matching your search criteria

### Dashboard
1. Access the dashboard at `https://your-codespace-url.com/dashboard`
2. View analytics including:
   - Top search queries
   - API response times by hour
   - Device/browser statistics
   - Recent request logs

## Error Handling
The application handles various error conditions:
- Invalid input validation on both client and server
- Network failures with appropriate error messages
- Third-party API unavailability
- Invalid data from the third-party API

## Technologies Used
- Java Servlets for the web service
- JSP for the dashboard views
- MongoDB Atlas for data storage
- Android SDK for the mobile application
- Open Library API for book data