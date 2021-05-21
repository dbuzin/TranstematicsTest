package ru.dbuzin.transtelematicstest.models;

import com.google.gson.annotations.SerializedName;

public class UploadPhotoResponse {
    @SerializedName("server")
    private int server;

    @SerializedName("photo")
    private String photo;

    @SerializedName("hash")
    private String hash;

    public int getServer() {
        return server;
    }

    public String getPhoto() {
        return photo;
    }

    public String getHash() {
        return hash;
    }
}
