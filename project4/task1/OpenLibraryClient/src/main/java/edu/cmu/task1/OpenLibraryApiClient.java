package edu.cmu.task1;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * A simple Java application that makes a request to the Open Library API
 * and extracts data from the response.
 * 
 * Author: YOUR_NAME YOUR_ANDREW_ID
 */
public class OpenLibraryApiClient {
    
    private static final String API_BASE_URL = "https://openlibrary.org/search.json";
    
    public static void main(String[] args) {
        try {
            // Hardcoded search term for demonstration purposes
            // In real Task 1, you would use Scanner to get user input
            String bookTitle = "Harry Potter";
            System.out.println("Demo search: \"" + bookTitle + "\"");
            
            // Make the API request
            String searchResult = searchBooks(bookTitle);
            
            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(searchResult);
            
            // Extract and display information
            int numFound = jsonResponse.getInt("numFound");
            System.out.println("Number of books found: " + numFound);
            
            if (numFound > 0) {
                JSONArray docs = jsonResponse.getJSONArray("docs");
                System.out.println("\nTop 5 results:");
                
                int limit = Math.min(5, docs.length());
                for (int i = 0; i < limit; i++) {
                    JSONObject book = docs.getJSONObject(i);
                    
                    // Extract book information
                    String title = book.getString("title");
                    
                    // Author might not exist for all books
                    String author = "Unknown";
                    if (book.has("author_name")) {
                        JSONArray authors = book.getJSONArray("author_name");
                        if (authors.length() > 0) {
                            author = authors.getString(0);
                        }
                    }
                    
                    // First publish year might not exist for all books
                    String publishYear = "Unknown";
                    if (book.has("first_publish_year")) {
                        publishYear = String.valueOf(book.getInt("first_publish_year"));
                    }
                    
                    System.out.printf("%d. Title: %s | Author: %s | First Published: %s%n", 
                            i+1, title, author, publishYear);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Makes a request to the Open Library API to search for books.
     * 
     * @param query The search query
     * @return The JSON response as a String
     * @throws Exception if there is an error making the request
     */
    private static String searchBooks(String query) throws Exception {
        // Encode the query parameter
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
        
        // Create the URL
        URL url = new URL(API_BASE_URL + "?q=" + encodedQuery);
        
        // Open a connection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        // Set request method
        connection.setRequestMethod("GET");
        
        // Set headers
        connection.setRequestProperty("Accept", "application/json");
        
        // Check the response code
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("HTTP Response Code: " + responseCode);
        }
        
        // Read the response
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        
        return response.toString();
    }
}