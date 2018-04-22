package com.aslammaududy.realtimetranslator.model;

import com.google.firebase.database.Exclude;

public class User {
    public final String NODE_USERS = "users";
    public final String NODE_MESSAGE = "message";
    public final String INITIAL_MESSAGE = "assalamu'alaikum";

    private String uid, name, message;

    public User() {

    }

    public User(String name, String message) {
        this.name = name;
        this.message = message;
    }

    @Exclude
    public String setUid(String uid) {
        return this.uid = uid;
    }

    @Exclude
    public String getUid() {
        return this.uid;
    }

    public String getName() {
        return this.name;
    }

    public String setMessage(String message) {
        return this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
