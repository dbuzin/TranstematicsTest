package ru.dbuzin.transtelematicstest.models;

import com.google.gson.annotations.SerializedName;

public class UploadUrlResponse {
    @SerializedName("response")
    UploadUrl uploadUrl;

    public UploadUrl getUploadUrl() {
        return uploadUrl;
    }
}
