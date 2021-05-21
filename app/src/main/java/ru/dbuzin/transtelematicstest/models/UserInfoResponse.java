package ru.dbuzin.transtelematicstest.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserInfoResponse {
    @SerializedName("response")
    ArrayList<UserInfo> userInfo;

    public ArrayList<UserInfo> getUserInfo() {
        return userInfo;
    }
}
