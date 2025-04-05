# Project 4 - Task 1

This directory contains the code for Task 1 of Project 4, demonstrating the use of a 3rd party API and MongoDB Atlas.

## Project Structure

- `src/main/java/edu/cmu/task1/OpenLibraryApiClient.java`: A Java application that makes requests to the Open Library API and extracts data.
- `src/main/java/edu/cmu/task1/RealMongoDBClient.java`: A Java application that connects to MongoDB Atlas, writes data, and reads data.
- `pom.xml`: Maven configuration file with required dependencies.
- `run_task1_demo.sh`: Script to compile and run both applications.
- `MONGODB_SETUP.md`: Instructions for setting up MongoDB Atlas.
- `Task1_Document_Template.md`: Template for the Task 1 submission document.

## Getting Started

1. **MongoDB Atlas Setup**: 
   - Follow the instructions in `MONGODB_SETUP.md` to set up MongoDB Atlas.
   - Get your connection string and update it in `RealMongoDBClient.java`.

2. **Run the Demo**:
   - Make sure the script is executable: `chmod +x run_task1_demo.sh`
   - Run the script: `./run_task1_demo.sh`
   - The script will:
     - Compile the project
     - Run the Open Library API client
     - Run the MongoDB client

3. **Take Screenshots**:
   - Take screenshots of both applications' outputs for your submission.

4. **Prepare Your Submission**:
   - Fill in the `Task1_Document_Template.md` with your name, Andrew ID, and add your screenshots.
   - Convert the document to PDF.
   - Submit the PDF to Canvas before the deadline (Monday March 24, 2:00pm).

## Requirements Fulfilled

This code fulfills the Task 1 requirements:

1. **Fetch data from a 3rd party API**:
   - The `OpenLibraryApiClient.java` makes a request to the Open Library API and receives JSON data.
   - It extracts and prints book information to the console.

2. **Write and read data to MongoDB Atlas**:
   - The `RealMongoDBClient.java` connects to MongoDB Atlas.
   - It prompts the user for a string, stores it in the database, and reads all stored documents.

## Notes

- Make sure to replace `YOUR_NAME` and `YOUR_ANDREW_ID` in the Java files with your actual name and Andrew ID.
- The Open Library API was chosen as it provides structured JSON data about books and is not on the banned list.
- The MongoDB client uses the MongoDB Java Driver to connect to MongoDB Atlas.