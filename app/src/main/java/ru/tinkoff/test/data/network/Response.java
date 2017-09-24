package ru.tinkoff.test.data.network;


import com.google.gson.annotations.SerializedName;

public abstract class Response {
    private static final String RESULT_CODE_OK = "OK";

    private static final String KEY_RESULT_CODE = "resultCode";
    protected static final String KEY_PAYLOAD = "payload";

    @SerializedName(KEY_RESULT_CODE)
    private String mResultCode;

    public String getResultCode() {
        return mResultCode;
    }

    public void setResultCode(String resultCode) {
        mResultCode = resultCode;
    }

    public boolean isSuccessful() {
        return RESULT_CODE_OK.equals(mResultCode);
    }
}
