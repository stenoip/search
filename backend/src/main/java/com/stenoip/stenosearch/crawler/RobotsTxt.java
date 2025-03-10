package com.stenoip.stenosearch.crawler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RobotsTxt {
    private static final Map<String, String[]> disallowedPathsCache = new HashMap<>();

    public static boolean isAllowed(String url) {
        try {
            String baseUrl = getBaseDomain(url);

            if (!disallowedPathsCache.containsKey(baseUrl)) {
                fetchAndCacheRobotsTxt(baseUrl);
            }

            String[] disallowedPaths = disallowedPathsCache.get(baseUrl);
            for (String disallowedPath : disallowedPaths) {
                if (url.startsWith(baseUrl + disallowedPath)) {
                    return false;
                }
            }
        } catch (Exception e) {
            System.err.println("Error processing robots.txt for: " + url + " - " + e.getMessage());
        }
        return true;
    }

    private static void fetchAndCacheRobotsTxt(String baseUrl) {
        try {
            URL robotsUrl = new URL(baseUrl + "/robots.txt");
            HttpURLConnection connection = (HttpURLConnection) robotsUrl.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder robotsContent = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                robotsContent.append(line).append("\n");
            }
            reader.close();

            disallowedPathsCache.put(baseUrl, parseDisallowedPaths(robotsContent.toString()));
        } catch (Exception e) {
            System.err.println("Could not fetch robots.txt for: " + baseUrl + " - " + e.getMessage());
            disallowedPathsCache.put(baseUrl, new String[0]);
        }
    }

    private static String[] parseDisallowedPaths(String robotsTxtContent) {
        return robotsTxtContent.lines()
                .filter(line -> line.startsWith("Disallow:"))
                .map(line -> line.split(":")[1].trim())
                .toArray(String[]::new);
    }

    private static String getBaseDomain(String url) throws Exception {
        URL fullUrl = new URL(url);
        return fullUrl.getProtocol() + "://" + fullUrl.getHost();
    }
}
