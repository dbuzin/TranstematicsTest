package ru.dbuzin.transtelematicstest.models;

import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;

    public String getTitle() {
        return title;
    }
}
