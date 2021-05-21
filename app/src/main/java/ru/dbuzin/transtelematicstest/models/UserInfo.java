package ru.dbuzin.transtelematicstest.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import ru.dbuzin.transtelematicstest.R;

import static androidx.room.ForeignKey.CASCADE;

@Entity
@ForeignKey(entity = FriendsItem.class, parentColumns = "id", childColumns = "id", onDelete = CASCADE)
public class UserInfo {
    @SerializedName("first_name")
    private String firstName;

    @PrimaryKey
    @SerializedName("id")
    private Long id;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("sex")
    private int sex;

    @SerializedName("bdate")
    private String bdate;

    @SerializedName("city")
    private Location city;

    @SerializedName("country")
    private Location country;

    @SerializedName("photo_200")
    private String avatarUrl;

    @SerializedName("photo_200_orig")
    private String avatarOrigUrl;

    @SerializedName("mobile_phone")
    private String mobilePhone;

    @SerializedName("status")
    private String status;

    @SerializedName("followers_count")
    private int followers;

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getSex() {
        if(sex == 1)
            return R.string.female;
        else
            return R.string.male;
    }

    public String getBdate() {
        return bdate;
    }

    public Location getCity() {
        return city;
    }

    public Location getCountry() {
        return country;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getAvatarOrigUrl() {
        return avatarOrigUrl;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public String getStatus() {
        return status;
    }

    public int getFollowers() {
        return followers;
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

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public void setCity(Location city) {
        this.city = city;
    }

    public void setCountry(Location country) {
        this.country = country;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setAvatarOrigUrl(String avatarOrigUrl) {
        this.avatarOrigUrl = avatarOrigUrl;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }
}
