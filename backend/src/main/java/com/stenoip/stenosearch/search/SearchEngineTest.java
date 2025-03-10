package com.stenoip.stenosearch.search;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

public class SearchEngineTest {
    public static void main(String[] args) {
        try (RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("localhost", 9200, "http")))) {
            
            SearchEngine searchEngine = new SearchEngine(client);
            
            // Perform a search query
            searchEngine.search("stenosearch", "example");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
