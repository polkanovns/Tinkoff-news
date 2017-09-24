package ru.tinkoff.test.data;


import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class News {
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_CREATION_DATE = "creationDate";
    private static final String KEY_LAST_MODIFICATION_DATE = "lastModificationDate";
    private static final String KEY_BANK_INFO_TYPE_ID = "bankInfoTypeId";
    private static final String KEY_TYPE_ID = "typeId";

    @SerializedName(KEY_TITLE)
    private NewsTitle mTitle;

    @SerializedName(KEY_CONTENT)
    private String mContent;

    @SerializedName(KEY_CREATION_DATE)
    private Date mCreationDate;

    @SerializedName(KEY_LAST_MODIFICATION_DATE)
    private Date mLastModificationDate;

    @SerializedName(KEY_BANK_INFO_TYPE_ID)
    private int mBankInfoTypeId;

    @SerializedName(KEY_TYPE_ID)
    private String mTypeId;

    public NewsTitle getTitle() {
        return mTitle;
    }

    public void setTitle(NewsTitle title) {
        mTitle = title;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public @Nullable Date getCreationDate() {
        return mCreationDate;
    }

    public void setCreationDate(Date creationDate) {
        mCreationDate = creationDate;
    }

    public @Nullable String getTypeId() {
        return mTypeId;
    }

    public void setTypeId(String typeId) {
        mTypeId = typeId;
    }

    public int getBankInfoTypeId() {
        return mBankInfoTypeId;
    }

    public void setBankInfoTypeId(int bankInfoTypeId) {
        mBankInfoTypeId = bankInfoTypeId;
    }

    public @Nullable Date getLastModificationDate() {
        return mLastModificationDate;
    }

    public void setLastModificationDate(Date lastModificationDate) {
        mLastModificationDate = lastModificationDate;
    }

    public @Nullable String getContent() {
        return mContent;
    }
}
