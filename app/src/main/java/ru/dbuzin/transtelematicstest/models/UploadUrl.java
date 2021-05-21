package ru.dbuzin.transtelematicstest.models;

import com.google.gson.annotations.SerializedName;

public class UploadUrl {
    @SerializedName("upload_url")
    private String uploadUrl;

    public String getUploadUrl() {
        return uploadUrl;
    }
}
