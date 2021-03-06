package com.aslammaududy.realtimetranslator.model;

public class User {
    public final String NODE_USERS = "users";
    public final String NODE_MESSAGE = "message";
    public final String NODE_NAME = "name";
    public final String NODE_LANG = "lang";
    public final String NODE_UID = "uid";
    public final String NODE_CALL = "call";
    public final String NODE_CALLER = "caller";
    public final String INITIAL_LANG = "en";
    public final String INITIAL_MESSAGE = "hello world";

    public static final int INITIAL_CALL = 0;
    public static final int CALLING = 1;
    public static final int ON_CALL = 2;

    private String uid, name, message, lang, caller;
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

    public String setCaller(String caller) {
        return this.caller = caller;
    }

    public String getCaller() {
        return caller;
    }
}
