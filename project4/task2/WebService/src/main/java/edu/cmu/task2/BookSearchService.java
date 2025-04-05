package edu.cmu.task2;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Servlet that provides book search functionality using the Open Library API
 * and logs requests to MongoDB Atlas.
 * 
 * Author: Jung Ho Park, junghop
 */
@WebServlet("/api/books")
public class BookSearchService extends HttpServlet {

    private static final String API_BASE_URL = "https://openlibrary.org/search.json";
    private static final String CONNECTION_STRING = "mongodb+srv://coder87:09870987@cluster0code.1e1ua.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0code";

    private MongoClient mongoClient;
    private MongoCollection<Document> logsCollection;

    @Override
    public void init() throws ServletException {
        try {
            // Initialize MongoDB connection
            mongoClient = MongoClients.create(CONNECTION_STRING);
            MongoDatabase database = mongoClient.getDatabase("booksearchapp");
            logsCollection = database.getCollection("logs");
        } catch (Exception e) {
            getServletContext().log("Error initializing MongoDB connection", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Extract query parameter
        String query = request.getParameter("q");
        String limit = request.getParameter("limit");
        int resultLimit = 5; // Default limit

        if (limit != null && !limit.isEmpty()) {
            try {
                resultLimit = Integer.parseInt(limit);
                if (resultLimit < 1)
                    resultLimit = 5;
                if (resultLimit > 20)
                    resultLimit = 20;
            } catch (NumberFormatException e) {
                // Ignore and use default
            }
        }

        if (query == null || query.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"error\": \"Missing query parameter\"}");
            out.flush();
            return;
        }

        try {
            // Log the incoming request
            Document logEntry = createLogEntry(request, query);

            // Make the API request
            long apiRequestStartTime = System.currentTimeMillis();
            String searchResult = searchBooks(query);
            long apiRequestDuration = System.currentTimeMillis() - apiRequestStartTime;

            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(searchResult);
            int numFound = jsonResponse.getInt("numFound");

            // Create response JSON
            JSONObject responseJson = new JSONObject();
            responseJson.put("query", query);
            responseJson.put("numFound", numFound);

            JSONArray booksArray = new JSONArray();
            if (numFound > 0) {
                JSONArray docs = jsonResponse.getJSONArray("docs");
                int resultsToShow = Math.min(resultLimit, docs.length());

                for (int i = 0; i < resultsToShow; i++) {
                    JSONObject book = docs.getJSONObject(i);
                    JSONObject bookInfo = new JSONObject();

                    // Extract book information
                    bookInfo.put("title", book.getString("title"));

                    // Author might not exist for all books
                    if (book.has("author_name")) {
                        JSONArray authors = book.getJSONArray("author_name");
                        if (authors.length() > 0) {
                            bookInfo.put("author", authors.getString(0));
                        } else {
                            bookInfo.put("author", "Unknown");
                        }
                    } else {
                        bookInfo.put("author", "Unknown");
                    }

                    // First publish year might not exist for all books
                    if (book.has("first_publish_year")) {
                        bookInfo.put("publishYear", book.getInt("first_publish_year"));
                    } else {
                        bookInfo.put("publishYear", "Unknown");
                    }

                    // Add cover ID if available
                    if (book.has("cover_i")) {
                        bookInfo.put("coverId", book.getInt("cover_i"));
                    }

                    // Add book key if available (for more details)
                    if (book.has("key")) {
                        bookInfo.put("key", book.getString("key"));
                    }

                    booksArray.put(bookInfo);
                }
            }

            responseJson.put("books", booksArray);

            // Update log entry with API response info
            updateLogEntry(logEntry, apiRequestDuration, numFound, responseJson);

            // Write response
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(responseJson.toString());
            out.flush();

        } catch (Exception e) {
            getServletContext().log("Error processing request", e);

            // Return error response
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
            out.flush();
        }
    }

    /**
     * Makes a request to the Open Library API to search for books.
     * 
     * @param query The search query
     * @return The JSON response as a String
     * @throws Exception if there is an error making the request
     */
    private String searchBooks(String query) throws Exception {
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

    /**
     * Creates a log entry document for the incoming request
     */
    private Document createLogEntry(HttpServletRequest request, String query) {
        Document logEntry = new Document();

        // Request information
        logEntry.append("timestamp", new Date());
        logEntry.append("query", query);
        logEntry.append("clientIp", request.getRemoteAddr());
        logEntry.append("userAgent", request.getHeader("User-Agent"));
        logEntry.append("requestPath", request.getRequestURI());

        // Try to save the log entry
        try {
            logsCollection.insertOne(logEntry);
        } catch (Exception e) {
            getServletContext().log("Error saving log entry", e);
        }

        return logEntry;
    }

    /**
     * Updates the log entry with API response information
     */
    private void updateLogEntry(Document logEntry, long apiRequestDuration,
            int numResults, JSONObject responseJson) {
        try {
            Document update = new Document();
            update.append("apiRequestDuration", apiRequestDuration);
            update.append("numResults", numResults);
            update.append("responseSize", responseJson.toString().length());

            logsCollection.updateOne(
                    new Document("_id", logEntry.get("_id")),
                    new Document("$set", update));
        } catch (Exception e) {
            getServletContext().log("Error updating log entry", e);
        }
    }

    @Override
    public void destroy() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}