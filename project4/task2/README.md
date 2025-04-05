# Project 4 - Book Search Application

This repository contains the web service component of the Book Search Application for Project 4.

## Deploying with GitHub Codespaces

1. Click on the green "Code" button above
2. Select the "Codespaces" tab
3. Click "Create codespace on master"
4. Wait for the Codespace to initialize (this may take a few minutes)
5. Once running, click on the "PORTS" tab at the bottom of the screen
6. Find port 8080, right-click on it, and select "Port Visibility" → "Public"
7. Click on the globe icon next to port 8080 to open the web service in a new tab

## Using the Application

- **Web Service API:** Access `/api/books?q=YOUR_SEARCH_TERM` to search for books
- **Dashboard:** Access `/dashboard` to view analytics and logs

## Android Application

The Android application is available in the separate BookSearchApp.zip file. 
To use it:

1. Extract the ZIP file
2. Open the project in Android Studio
3. Update the WEB_SERVICE_URL constant in MainActivity.java with your Codespace URL
4. Build and run the app on your Android device or emulator

## Project Structure

- `ROOT.war`: The compiled web application
- `Dockerfile`: Container configuration for GitHub Codespaces
- `.devcontainer.json`: Codespaces configuration file