package it.sal.disco.unimib.charityhub.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Objects;

public class User implements Parcelable {
    private String name;
    private String email;
    private String Uid;
    private String countryOfInterest;

    public User() {}
    public User(String name, String email, String uid) {
        this.name = name;
        this.email = email;
        Uid = uid;
    }

    public User(String name, String email, String uid, String countryOfInterest) {
        this.name = name;
        this.email = email;
        Uid = uid;
        this.countryOfInterest = countryOfInterest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(Uid, user.Uid) && Objects.equals(countryOfInterest, user.countryOfInterest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, Uid, countryOfInterest);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getCountryOfInterest() {
        return countryOfInterest;
    }

    public void setCountryOfInterest(String countryOfInterest) {
        this.countryOfInterest = countryOfInterest;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", Uid='" + Uid + '\'' +
                ", countryOfInterest='" + countryOfInterest + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.Uid);
        dest.writeString(this.countryOfInterest);
    }

    public void readFromParcel(Parcel source) {
        this.name = source.readString();
        this.email = source.readString();
        this.Uid = source.readString();
        this.countryOfInterest = source.readString();
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.email = in.readString();
        this.Uid = in.readString();
        this.countryOfInterest = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
