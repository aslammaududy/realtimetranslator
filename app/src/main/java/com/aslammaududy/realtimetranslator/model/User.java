package com.aslammaududy.realtimetranslator.model;

import com.google.firebase.database.Exclude;

public class User {
    public final String NODE_USERS = "users";
    public final String NODE_MESSAGE = "message";
    public final String NODE_NAME = "name";
    public final String NODE_LANG = "lang";
    public final String NODE_RESULT = "result";
    public final String INITIAL_LANG = "en";
    public final String INITIAL_MESSAGE = "hello world";
    public final String INITIAL_RESULT = "halo dunia";

    private String uid, name, message, lang, result;

    public User() {

    }

    @Exclude
    public String setUid(String uid) {
        return this.uid = uid;
    }

    @Exclude
    public String getUid() {
        return this.uid;
    }

    public String setName(String name) {
        return this.name = name;
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

    public String setLang(String lang) {
        return this.lang = lang;
    }

    public String getLang() {
        return this.lang;
    }

    public String setResult(String result) {
        return this.result = result;
    }

    public String getResult() {
        return this.result;
    }
}
