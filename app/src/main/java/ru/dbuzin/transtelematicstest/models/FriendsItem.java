package ru.dbuzin.transtelematicstest.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class FriendsItem {
    @SerializedName("first_name")
    private String firstName;

    @PrimaryKey
    @SerializedName("id")
    private Long id;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("photo_100")
    private String photoUrl;

    @SerializedName("online")
    private int online;

    @SerializedName("track_code")
    private String trackCode;

    public String getFirstName() {
        return firstName;
    }

    public Long getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public int getOnline() {
        return online;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getTrackCode() {
        return trackCode;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public void setTrackCode(String trackCode) {
        this.trackCode = trackCode;
    }
}
