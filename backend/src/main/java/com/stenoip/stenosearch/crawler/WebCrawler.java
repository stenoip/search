package com.stenoip.stenosearch.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class WebCrawler {
    private static final int MAX_DEPTH = 2; // Maximum depth for crawling
    private Set<String> visitedUrls = new HashSet<>(); // Track visited URLs

    public void crawl(String url, int depth) {
        if (depth > MAX_DEPTH || visitedUrls.contains(url)) {
            return; // Stop recursion if max depth reached or URL already visited
        }

        try {
            // Mark the URL as visited
            visitedUrls.add(url);

            // Fetch and parse the page
            Document document = Jsoup.connect(url).get();
            System.out.println("Crawling URL: " + url);
            System.out.println("Page Title: " + document.title());

            // Extract and process links
            for (Element link : document.select("a[href]")) {
                String nextUrl = link.absUrl("href"); // Get absolute URL
                if (!visitedUrls.contains(nextUrl) && nextUrl.startsWith("http")) {
                    crawl(nextUrl, depth + 1); // Recursively crawl the next URL
                }
            }
        } catch (IOException e) {
            System.err.println("Error crawling URL: " + url + " - " + e.getMessage());
        }
    }
}
