package ru.tinkoff.test.data;


import com.google.gson.annotations.SerializedName;

public class Date {
    private static final String KEY_MILLIS = "milliseconds";

    @SerializedName(KEY_MILLIS)
    private long milliseconds;

    public long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
    }
}
