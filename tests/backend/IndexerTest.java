package com.stenoip.stenosearch.index;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.Map;

public class Indexer {
    private RestHighLevelClient client;

    public Indexer(RestHighLevelClient client) {
        this.client = client;
    }

    public void createIndex(String indexName, Map<String, Object> document) {
        try {
            IndexRequest request = new IndexRequest(indexName);
            request.source(document, XContentType.JSON);

            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            System.out.println("Indexed document ID: " + response.getId());
        } catch (IOException e) {
            System.err.println("Error indexing document: " + e.getMessage());
        }
    }
}
