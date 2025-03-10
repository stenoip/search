package com.stenoip.stenosearch.index;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.HashMap;
import java.util.Map;

public class IndexerTest {
    public static void main(String[] args) {
        try (RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("localhost", 9200, "http")))) {
            
            Indexer indexer = new Indexer(client);
            
            // Example document to index
            Map<String, Object> document = new HashMap<>();
            document.put("url", "https://example.com");
            document.put("title", "Example Page");
            document.put("content", "This is a sample document for StenoSearch.");
            document.put("metadata", Map.of(
                "keywords", "example, test, sample",
                "author", "Stenoip",
                "date", "2025-03-10"
            ));
            document.put("crawl_status", "crawled");
            document.put("last_crawled", "2025-03-10");

            // Index the document
            indexer.createIndex("stenosearch", document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
