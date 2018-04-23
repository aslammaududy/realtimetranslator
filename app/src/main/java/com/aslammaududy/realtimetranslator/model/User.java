package com.aslammaududy.realtimetranslator.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public final String NODE_USERS = "users";
    public final String NODE_MESSAGE = "message";
    public final String INITIAL_MESSAGE = "hello world";
    public final String INITIAL_LANGUAGE = "en";

    private String uid, name, message, lang;

    public User() {

    }

    public User(String name, String message, String lang) {
        this.name = name;
        this.message = message;
        this.lang = lang;
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

    public String getLang() {
        return this.lang;
    }
}
