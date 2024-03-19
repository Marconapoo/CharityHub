package it.sal.disco.unimib.charityhub.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class User implements Parcelable {
    private String name;
    private String email;
    private String Uid;

    public User(String name, String email, String Uid) {
        this.name = name;
        this.email = email;
        this.Uid = Uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
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



    @NonNull
    @Override
    public String toString() {
        return "{ name : " + name + "\nemail: " + email;
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
    }

    public void readFromParcel(Parcel source) {
        this.name = source.readString();
        this.email = source.readString();
        this.Uid = source.readString();
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.email = in.readString();
        this.Uid = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
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
