package ru.dbuzin.transtelematicstest.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Friends {
    @SerializedName("count")
    private int count;

    @SerializedName("items")
    private ArrayList<FriendsItem> friendsList;

    public int getCount() {
        return count;
    }

    public ArrayList<FriendsItem> getFriendsList() {
        return friendsList;
    }
}
