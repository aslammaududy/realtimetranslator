package com.aslammaududy.realtimetranslator.model;

public class User {
    public final String NODE_USERS = "users";
    public final String NODE_MESSAGE = "message";
    public final String NODE_NAME = "name";
    public final String NODE_LANG = "lang";
    public final String NODE_UID = "uid";
    public final String NODE_CALL = "call";
    public final String INITIAL_LANG = "en";
    public final String INITIAL_MESSAGE = "hello world";
    public final int INITIAL_CALL = 0;

    private String uid, name, message, lang;
    private int call;

    public User() {

    }

    public String setUid(String uid) {
        return this.uid = uid;
    }

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

    public int setCall(int call) {
        return this.call = call;
    }

    public int getCall() {
        return call;
    }
}
