package ru.tinkoff.test.data;


import com.google.gson.annotations.SerializedName;

public class NewsTitle {
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TEXT = "text";
    private static final String KEY_PUBLICATION_DATE = "publicationDate";
    private static final String KEY_BANK_INFO_TYPE_ID = "bankInfoTypeId";

    @SerializedName(KEY_ID)
    private String mId;

    @SerializedName(KEY_NAME)
    private String mName;

    @SerializedName(KEY_TEXT)
    private String mText;

    @SerializedName(KEY_PUBLICATION_DATE)
    private Date mPublicationDate;

    @SerializedName(KEY_BANK_INFO_TYPE_ID)
    private int mBankInfoTypeId;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public Date getPublicationDate() {
        return mPublicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.mPublicationDate = publicationDate;
    }

    public int getBankInfoTypeId() {
        return mBankInfoTypeId;
    }

    public void setBankInfoTypeId(int bankInfoTypeId) {
        this.mBankInfoTypeId = bankInfoTypeId;
    }

}
