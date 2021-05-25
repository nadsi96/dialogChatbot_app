package com.example.dialogflowbot.rich_response;

public class Suggestion_Chips {
    private String text;
    private String suggestions[];

    public Suggestion_Chips(String text, String suggestions[]){
        this.text = text;
        this.suggestions = suggestions;
    }

    public String getText() {
        return text;
    }
    public String[] getSuggestions() {
        return suggestions;
    }
}
