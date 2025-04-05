<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.bson.Document" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<!DOCTYPE html>
<html>
<head>
    <title>Book Search Dashboard</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
        }
        h1 {
            color: #333;
            text-align: center;
            margin-bottom: 30px;
        }
        .dashboard-section {
            background-color: white;
            border-radius: 5px;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        h2 {
            color: #2c3e50;
            margin-top: 0;
            border-bottom: 1px solid #eee;
            padding-bottom: 10px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th {
            background-color: #4b6cb7;
            color: white;
            text-align: left;
            padding: 12px;
        }
        td {
            padding: 10px;
            border-bottom: 1px solid #ddd;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        .analytics-container {
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
        }
        .analytics-box {
            flex: 1 1 30%;
            min-width: 300px;
            margin: 10px;
        }
        @media (max-width: 768px) {
            .analytics-box {
                flex: 1 1 100%;
            }
        }
    </style>
</head>
<body>

<div class="container">
    <h1>Book Search Dashboard</h1>
    
    <div class="analytics-container">
        <!-- Top Searches -->
        <div class="analytics-box dashboard-section">
            <h2>Top Search Queries</h2>
            <table>
                <thead>
                    <tr>
                        <th>Query</th>
                        <th>Count</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                    List<Document> topSearches = (List<Document>) request.getAttribute("topSearches");
                    if (topSearches != null && !topSearches.isEmpty()) {
                        for (Document doc : topSearches) {
                    %>
                    <tr>
                        <td><%= doc.get("_id") %></td>
                        <td><%= doc.get("count") %></td>
                    </tr>
                    <% 
                        }
                    } else {
                    %>
                    <tr>
                        <td colspan="2">No search data available</td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
        
        <!-- Response Time Stats -->
        <div class="analytics-box dashboard-section">
            <h2>API Response Times by Hour</h2>
            <table>
                <thead>
                    <tr>
                        <th>Hour</th>
                        <th>Avg. Response Time (ms)</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                    List<Document> responseTimeStats = (List<Document>) request.getAttribute("responseTimeStats");
                    if (responseTimeStats != null && !responseTimeStats.isEmpty()) {
                        for (Document doc : responseTimeStats) {
                            Document idDoc = (Document) doc.get("_id");
                            Integer hour = idDoc.getInteger("hour");
                            Double avgDuration = doc.getDouble("avgDuration");
                    %>
                    <tr>
                        <td><%= hour %>:00</td>
                        <td><%= String.format("%.2f", avgDuration) %></td>
                    </tr>
                    <% 
                        }
                    } else {
                    %>
                    <tr>
                        <td colspan="2">No response time data available</td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
        
        <!-- User Agent Stats -->
        <div class="analytics-box dashboard-section">
            <h2>Top Devices/Browsers</h2>
            <table>
                <thead>
                    <tr>
                        <th>User Agent</th>
                        <th>Count</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                    List<Document> userAgentStats = (List<Document>) request.getAttribute("userAgentStats");
                    if (userAgentStats != null && !userAgentStats.isEmpty()) {
                        for (Document doc : userAgentStats) {
                            String userAgent = doc.getString("_id");
                            // Truncate long user agent strings
                            if (userAgent != null && userAgent.length() > 50) {
                                userAgent = userAgent.substring(0, 47) + "...";
                            }
                    %>
                    <tr>
                        <td><%= userAgent %></td>
                        <td><%= doc.get("count") %></td>
                    </tr>
                    <% 
                        }
                    } else {
                    %>
                    <tr>
                        <td colspan="2">No user agent data available</td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
    
    <!-- Recent Logs -->
    <div class="dashboard-section">
        <h2>Recent Request Logs</h2>
        <table>
            <thead>
                <tr>
                    <th>Timestamp</th>
                    <th>Query</th>
                    <th>Client IP</th>
                    <th>Results</th>
                    <th>API Duration (ms)</th>
                </tr>
            </thead>
            <tbody>
                <% 
                List<Document> recentLogs = (List<Document>) request.getAttribute("recentLogs");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                
                if (recentLogs != null && !recentLogs.isEmpty()) {
                    for (Document log : recentLogs) {
                        Date timestamp = log.getDate("timestamp");
                        String formattedDate = timestamp != null ? dateFormat.format(timestamp) : "N/A";
                        String query = log.getString("query");
                        String clientIp = log.getString("clientIp");
                        Integer numResults = log.getInteger("numResults");
                        Long apiDuration = log.getLong("apiRequestDuration");
                %>
                <tr>
                    <td><%= formattedDate %></td>
                    <td><%= query != null ? query : "N/A" %></td>
                    <td><%= clientIp != null ? clientIp : "N/A" %></td>
                    <td><%= numResults != null ? numResults : "N/A" %></td>
                    <td><%= apiDuration != null ? apiDuration : "N/A" %></td>
                </tr>
                <% 
                    }
                } else {
                %>
                <tr>
                    <td colspan="5">No logs available</td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
</div>

</body>
</html>