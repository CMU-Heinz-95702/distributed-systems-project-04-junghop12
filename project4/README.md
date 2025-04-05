# Project 4: Book Search Application

## Jung Ho Park/ junghop

## Project Organization

This project is organized into two tasks as per the requirements:

### Task 1: Project Feasibility Demo
Located in the `task1` folder:
- `OpenLibraryClient` - IntelliJ project for the Open Library API client
- `MongoDBClient` - IntelliJ project for the MongoDB Atlas client
- `Task1_Writeup.txt` - The written report for Task 1
- Supporting files: `TASK1_README.md`, `MONGODB_SETUP.md`, and `run_task1_demo.sh`

### Task 2: Distributed Application and Dashboard
Located in the `task2` folder:
- `WebService` - IntelliJ project for the web service component
- `BookSearchApp.tar.gz` - Android Studio project for the mobile app
- `ROOT.war` - Compiled web application ready for deployment
- `Dockerfile` and `.devcontainer.json` - GitHub Codespaces configuration
- `Task2_Writeup.txt` - The written report for Task 2
- Supporting files: `TASK2_README.md`

## How to Run

### Task 1
1. Extract the IntelliJ projects
2. Open them in IntelliJ IDEA
3. Run the OpenLibraryApiClient.java to test the API connection
4. Run the RealMongoDBClient.java to test the MongoDB connection (update connection string if needed)

### Task 2
1. Deploy the web service:
   - Accept the GitHub Classroom assignment
   - Upload the files to the repository
   - Create a Codespace and make port 8080 public
   
2. Run the Android app:
   - Extract BookSearchApp.tar.gz
   - Open in Android Studio
   - Update the WEB_SERVICE_URL in MainActivity.java with your Codespace URL
   - Build and run on an Android device or emulator

## Technologies Used
- Java for web service and API clients
- Android SDK for mobile application
- MongoDB Atlas for cloud database
- Open Library API for book data
- GitHub Codespaces for deployment