package com.lorem_ipsum.models;

/**
 * Created by hoangminh on 12/22/15.
 */
public class MyLog {

    public static final int VERBOSE = 0;
    public static final int DEBUG = 1;
    public static final int INFO = 2;
    public static final int WARNING = 3;
    public static final int ERROR = 4;

    public String tag, message;
    public int logPriority;

    public MyLog(String tag, String message, int logPriority) {
        this.tag = tag;
        this.message = message;
        this.logPriority = logPriority;
    }
}
