# MongoDB Atlas Setup for Task 1

## Step 1: Create MongoDB Atlas Account and Cluster
1. Go to https://www.mongodb.com/atlas/database and create a free account.
2. Answer the "Tell us a few things..." questions however you like, but include Java as the preferred language.
3. Choose to create a FREE shared cluster.
4. Accept the default settings and click "Create Cluster".

## Step 2: Set Up Security
1. In the Security Quickstart:
   - **How would you like to authenticate your connection?**
     - Select "Username and Password"
     - Create a MongoDB username and password (only use letters and numbers to avoid encoding issues)
     - Note down these credentials
   - **Where would you like to connect from?**
     - Choose "My Local Environment"
     - Enter `0.0.0.0/0` for the IP address field (allows connection from anywhere, required for the project)
     - Click "Add Entry"
   - Click "Finish and Close"

## Step 3: Get Connection String
1. Once your cluster is created, click the "Connect" button.
2. Select "Connect your application".
3. Choose Java driver, version 4.3 or later.
4. Copy the connection string provided. It should look like:
   ```
   mongodb+srv://username:<password>@clusterXYZ.mongodb.net/?retryWrites=true&w=majority
   ```
5. Replace `<password>` with your actual password.

## Step 4: Update the MongoDB Client Code
1. Open the `RealMongoDBClient.java` file.
2. Replace the `CONNECTION_STRING` placeholder with your actual MongoDB Atlas connection string:
   ```java
   private static final String CONNECTION_STRING = "mongodb+srv://myuser:mypassword@cluster0.example.mongodb.net/?retryWrites=true&w=majority";
   ```
3. Make sure to replace `myuser`, `mypassword`, and the cluster URL with your actual values.

## Step 5: Run the Task 1 Demo
1. Make sure you have Maven installed.
2. Open a terminal in the project directory.
3. Run the demo script:
   ```
   ./run_task1_demo.sh
   ```
4. For the MongoDB part of the demo, when prompted, enter a string to store in MongoDB.
5. Take screenshots of the outputs for your Task 1 document.

## Troubleshooting Connection Issues
If you encounter SSL or connection issues with MongoDB Atlas:

1. Make sure your MongoDB Atlas cluster is properly set up with the IP whitelist (should include 0.0.0.0/0).
2. Try using the alternative connection format:
   ```
   mongodb://username:password@server1:27017,server2:27017,server3:27017/database?w=majority&retryWrites=true&tls=true&authMechanism=SCRAM-SHA-1
   ```
   You can get the server addresses from your MongoDB Atlas dashboard by clicking on the cluster and looking at the "Cluster" section.
3. If SSL issues persist, you can try adding SSL settings to your connection:
   ```java
   MongoClientSettings settings = MongoClientSettings.builder()
       .applyConnectionString(new ConnectionString(CONNECTION_STRING))
       .applyToSslSettings(builder -> builder.enabled(true).invalidHostNameAllowed(true))
       .build();
   MongoClient mongoClient = MongoClients.create(settings);
   ```