package com.stenoip.stenosearch.crawler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RobotsTxt {
    private static final Map<String, String[]> disallowedPathsCache = new HashMap<>();

    /**
     * Checks if a given URL is allowed for crawling based on robots.txt.
     * 
     * @param url The URL to check.
     * @return True if crawling is allowed, false otherwise.
     */
    public static boolean isAllowed(String url) {
        try {
            // Extract the base domain from the given URL
            String baseUrl = getBaseDomain(url);

            // Check if the disallowed paths for the domain are already cached
            if (!disallowedPathsCache.containsKey(baseUrl)) {
                fetchAndCacheRobotsTxt(baseUrl);
            }

            // Get disallowed paths for the domain
            String[] disallowedPaths = disallowedPathsCache.get(baseUrl);

            // Check if the URL matches any disallowed paths
            for (String disallowedPath : disallowedPaths) {
                if (url.startsWith(baseUrl + disallowedPath)) {
                    return false; // URL is disallowed
                }
            }
        } catch (Exception e) {
            System.err.println("Error processing robots.txt for: " + url + " - " + e.getMessage());
        }
        return true; // Default to true if no disallowed paths are found
    }

    /**
     * Fetches and caches the disallowed paths from robots.txt for a given domain.
     * 
     * @param baseUrl The base domain (e.g., https://example.com).
     */
    private static void fetchAndCacheRobotsTxt(String baseUrl) {
        try {
            URL robotsUrl = new URL(baseUrl + "/robots.txt");
            HttpURLConnection connection = (HttpURLConnection) robotsUrl.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder robotsContent = new StringBuilder();

            // Read the content of robots.txt
            while ((line = reader.readLine()) != null) {
                robotsContent.append(line).append("\n");
            }
            reader.close();

            // Parse disallowed paths
            disallowedPathsCache.put(baseUrl, parseDisallowedPaths(robotsContent.toString()));
        } catch (Exception e) {
            System.err.println("Could not fetch robots.txt for: " + baseUrl + " - " + e.getMessage());
            disallowedPathsCache.put(baseUrl, new String[0]); // Default to no disallowed paths
        }
    }

    /**
     * Parses the disallowed paths from the content of robots.txt.
     * 
     * @param robotsTxtContent The content of robots.txt.
     * @return An array of disallowed paths.
     */
    private static String[] parseDisallowedPaths(String robotsTxtContent) {
        return robotsTxtContent.lines()
                .filter(line -> line.startsWith("Disallow:"))
                .map(line -> line.split(":")[1].trim()) // Extract the path
                .toArray(String[]::new);
    }

    /**
     * Extracts the base domain from a full URL.
     * 
     * @param url The full URL.
     * @return The base domain (e.g., https://example.com).
     */
    private static String getBaseDomain(String url) throws Exception {
        URL fullUrl = new URL(url);
        return fullUrl.getProtocol() + "://" + fullUrl.getHost();
    }
}
