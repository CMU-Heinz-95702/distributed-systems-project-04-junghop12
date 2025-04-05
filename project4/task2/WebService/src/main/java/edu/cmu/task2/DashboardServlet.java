package edu.cmu.task2;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Servlet that provides a dashboard interface for the Book Search application.
 * Displays logs and analytics from MongoDB.
 * 
 * Author: Jung Ho Park, junghop
 */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

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

        try {
            // Get data for analytics
            List<Document> topSearches = getTopSearches();
            List<Document> responseTimeStats = getResponseTimeStats();
            List<Document> userAgentStats = getUserAgentStats();
            List<Document> recentLogs = getRecentLogs(100); // Get last 100 log entries

            // Set attributes for JSP
            request.setAttribute("topSearches", topSearches);
            request.setAttribute("responseTimeStats", responseTimeStats);
            request.setAttribute("userAgentStats", userAgentStats);
            request.setAttribute("recentLogs", recentLogs);

            // Forward to JSP
            request.getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            getServletContext().log("Error processing dashboard request", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error loading dashboard: " + e.getMessage());
        }
    }

    /**
     * Gets the top search queries by count
     */
    private List<Document> getTopSearches() {
        List<Document> result = new ArrayList<>();

        try {
            result = logsCollection.aggregate(Arrays.asList(
                    Aggregates.group("$query", Accumulators.sum("count", 1)),
                    Aggregates.sort(Sorts.descending("count")),
                    Aggregates.limit(10))).into(new ArrayList<>());
        } catch (Exception e) {
            getServletContext().log("Error getting top searches", e);
        }

        return result;
    }

    /**
     * Gets API response time statistics
     */
    private List<Document> getResponseTimeStats() {
        List<Document> result = new ArrayList<>();

        try {
            // Average response time by hour
            result = logsCollection.aggregate(Arrays.asList(
                    Aggregates.match(new Document("apiRequestDuration", new Document("$exists", true))),
                    Aggregates.group(
                            new Document("hour", new Document("$hour", "$timestamp")),
                            Accumulators.avg("avgDuration", "$apiRequestDuration")),
                    Aggregates.sort(Sorts.ascending("_id.hour")))).into(new ArrayList<>());
        } catch (Exception e) {
            getServletContext().log("Error getting response time stats", e);
        }

        return result;
    }

    /**
     * Gets statistics about user agents (device types)
     */
    private List<Document> getUserAgentStats() {
        List<Document> result = new ArrayList<>();

        try {
            result = logsCollection.aggregate(Arrays.asList(
                    Aggregates.group("$userAgent", Accumulators.sum("count", 1)),
                    Aggregates.sort(Sorts.descending("count")),
                    Aggregates.limit(5))).into(new ArrayList<>());
        } catch (Exception e) {
            getServletContext().log("Error getting user agent stats", e);
        }

        return result;
    }

    /**
     * Gets recent log entries
     */
    private List<Document> getRecentLogs(int limit) {
        List<Document> result = new ArrayList<>();

        try {
            result = logsCollection.find()
                    .sort(new Document("timestamp", -1))
                    .limit(limit)
                    .into(new ArrayList<>());
        } catch (Exception e) {
            getServletContext().log("Error getting recent logs", e);
        }

        return result;
    }

    @Override
    public void destroy() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}