#!/bin/bash

echo "===== Running Task 1 Demo ====="
echo "This script will compile and run both the OpenLibraryApiClient and RealMongoDBClient"

echo -e "\n===== Part 1: Open Library API Client ====="
echo "Compiling and running OpenLibraryApiClient..."
cd OpenLibraryClient
mvn clean compile
if [ $? -ne 0 ]; then
    echo "Compilation failed for OpenLibraryApiClient. Please fix the errors and try again."
    exit 1
fi
mvn exec:java -Dexec.mainClass="edu.cmu.task1.OpenLibraryApiClient"
cd ..

echo -e "\n\n===== Part 2: MongoDB Client ====="
echo "IMPORTANT: Ensure your MongoDB connection string is updated in MongoDBClient/src/main/java/edu/cmu/task1/RealMongoDBClient.java"
echo "Compiling and running RealMongoDBClient..."
cd MongoDBClient
mvn clean compile
if [ $? -ne 0 ]; then
    echo "Compilation failed for RealMongoDBClient. Please fix the errors and try again."
    exit 1
fi
mvn exec:java -Dexec.mainClass="edu.cmu.task1.RealMongoDBClient"
cd ..

echo -e "\n===== Task 1 Demo Completed ====="
echo "For your Task 1 submission, take screenshots of the above outputs and include them in your PDF document."