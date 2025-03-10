package com.stenoip.stenosearch.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class WebCrawler {
    private static final int MAX_DEPTH = 2;
    private Set<String> visitedUrls = new HashSet<>();

    public void crawl(String url, int depth) {
        if (depth > MAX_DEPTH || visitedUrls.contains(url)) {
            return;
        }

        try {
            visitedUrls.add(url);
            Document document = Jsoup.connect(url).get();
            System.out.println("Crawling URL: " + url);
            System.out.println("Page Title: " + document.title());

            for (Element link : document.select("a[href]")) {
                String nextUrl = link.absUrl("href");
                if (!visitedUrls.contains(nextUrl) && nextUrl.startsWith("http")) {
                    crawl(nextUrl, depth + 1);
                }
            }
        } catch (IOException e) {
            System.err.println("Error crawling URL: " + url + " - " + e.getMessage());
        }
    }
}
