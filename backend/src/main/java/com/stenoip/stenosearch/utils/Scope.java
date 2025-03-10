package com.stenoip.stenosearch;

public class Scope {
    private String type;
    private String description;

    public Scope(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return "Scope Type: " + type + " | Description: " + description;
    }
}
