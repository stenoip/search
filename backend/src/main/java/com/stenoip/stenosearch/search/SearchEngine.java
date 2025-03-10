package com.stenoip.stenosearch.search;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;

public class SearchEngine {
    private RestHighLevelClient client;

    public SearchEngine(RestHighLevelClient client) {
        this.client = client;
    }

    /**
     * Search the index for documents matching the query.
     *
     * @param indexName Name of the Elasticsearch index.
     * @param queryTerm User's search term.
     */
    public void search(String indexName, String queryTerm) {
        try {
            // Create a search request for the specified index
            SearchRequest searchRequest = new SearchRequest(indexName);

            // Build the query
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("title", queryTerm).boost(2.0f)) // Boost titles
                .should(QueryBuilders.matchQuery("content", queryTerm));         // Search content

            // Search source with sorting
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(boolQuery)
                .from(0) // Pagination: starting document
                .size(10) // Limit the number of results
                .sort("_score", SortOrder.DESC); // Sort by relevance score (default)

            // Attach the query to the search request
            searchRequest.source(searchSourceBuilder);

            // Execute the search
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            // Print the results
            searchResponse.getHits().forEach(hit -> {
                System.out.println("Score: " + hit.getScore());
                System.out.println("Document: " + hit.getSourceAsString());
            });
        } catch (IOException e) {
            System.err.println("Error executing search: " + e.getMessage());
        }
    }
}
