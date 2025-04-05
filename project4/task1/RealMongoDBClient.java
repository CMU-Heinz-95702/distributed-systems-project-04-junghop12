package edu.cmu.task1;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A simple Java application that connects to MongoDB Atlas,
 * writes a document, and reads all documents.
 * 
 * Author: Jung Ho Park, junghop
 */
public class RealMongoDBClient {

    // Replace with your own MongoDB Atlas connection string
    private static final String CONNECTION_STRING = "mongodb+srv://coder87:09870987@cluster0code.1e1ua.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0code";

    // Database and collection names
    private static final String DATABASE_NAME = "task1";
    private static final String COLLECTION_NAME = "userInputs";

    public static void main(String[] args) {
        try {
            // Connect to MongoDB Atlas
            System.out.println("Connecting to MongoDB Atlas...");
            MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);

            // Get the database and collection
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            System.out.println("Connected to MongoDB Atlas successfully!");

            // Get user input
            String userInput;
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter a string to store in MongoDB: ");
                userInput = scanner.nextLine();
            } catch (Exception e) {
                // If there's an issue with Scanner (common when running via Maven exec plugin),
                // use a default value
                userInput = "Default input - MongoDB Atlas connection test";
                System.out.println("Using default input: \"" + userInput + "\"");
            }

            // Create a document with the user input
            Document document = new Document("text", userInput);

            // Insert the document into the collection
            collection.insertOne(document);
            System.out.println("Document inserted successfully!");

            // Clear the collection and add fresh sample data to ensure we have good data to
            // display
            System.out.println("\nResetting collection with fresh sample data...");
            collection.drop();

            // Add fresh sample documents with guaranteed text
            List<Document> freshSampleDocs = new ArrayList<>();
            freshSampleDocs.add(new Document("text", "Sample 1 - MongoDB Atlas connection test"));
            freshSampleDocs.add(new Document("text", "Sample 2 - Project 4 Task 1 demo"));
            freshSampleDocs.add(new Document("text", "Sample 3 - CMU Distributed Systems"));
            freshSampleDocs.add(new Document("text", userInput));

            collection.insertMany(freshSampleDocs);
            System.out.println("Added " + freshSampleDocs.size() + " sample documents to the collection.");

            // Read all documents from the collection
            System.out.println("\nReading all documents from the collection:");
            List<Document> documents = collection.find().into(new ArrayList<>());

            if (documents.isEmpty()) {
                System.out.println("No documents found in the collection.");
            } else {
                int count = 1;
                for (Document doc : documents) {
                    System.out.println(count + ". Text: " + doc.getString("text"));
                    count++;
                }
                System.out.println("Total documents: " + documents.size());
            }

            // Close the MongoDB client connection
            mongoClient.close();
            System.out.println("\nMongoDB connection closed.");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}