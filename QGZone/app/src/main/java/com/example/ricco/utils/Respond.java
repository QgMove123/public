package com.example.ricco.utils;

/**
 * Created by Mr_Do on 2016/8/8.
 */
public class Respond {
    private String from;
    private String to;
    private String content;

    public Respond(String from, String to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
    }

    public String getContent() {

        return content;
    }

    public String getFrom() {

        return from;
    }

    public String getto() {

        return to;
    }
}
