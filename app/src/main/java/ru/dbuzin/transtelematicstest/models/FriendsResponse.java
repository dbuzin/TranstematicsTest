package ru.dbuzin.transtelematicstest.models;

import com.google.gson.annotations.SerializedName;

public class FriendsResponse {
    @SerializedName("response")
    private Friends friends;

    public Friends getFriends() {
        return friends;
    }
}
