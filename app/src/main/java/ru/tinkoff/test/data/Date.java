package ru.tinkoff.test.data;


import com.google.gson.annotations.SerializedName;

public class Date {
    private static final String KEY_MILLIS = "milliseconds";

    @SerializedName(KEY_MILLIS)
    private long mMilliseconds;

    public Date() {}

    public Date(long milliseconds) {
        mMilliseconds = milliseconds;
    }

    public long getMilliseconds() {
        return mMilliseconds;
    }

    public void setMilliseconds(long milliseconds) {
        this.mMilliseconds = milliseconds;
    }
}
