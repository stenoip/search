package com.stenoip.stenosearch;

public class Application {
    public static void main(String[] args) {
        System.out.println("Welcome to StenoSearch!");
        System.out.println("Defining the scope and purpose...");

        // Define the scope of the search engine
        Scope scope = new Scope("General", "Crawl and index text content across the web.");
        System.out.println(scope.getDescription());

        // Proceed with the next steps
        System.out.println("Initializing components...");
        System.out.println("Starting Web Crawler...");
        // Future calls for crawler, indexing, and search
    }
}
