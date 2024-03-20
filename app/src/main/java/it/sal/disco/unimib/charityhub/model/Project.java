package it.sal.disco.unimib.charityhub.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Project implements Parcelable {

    private int id;
    private boolean isActive;
    private String title;
    private String summary;
    private String country;
    private String city;
    private String imageUrl;
    private float goal;
    private float funding;
    private float remainingFunding;

    public Project(int id, boolean isActive, String title, String summary, String country, String city, String imageUrl, float goal, float funding, float remainingFunding) {
        this.id = id;
        this.isActive = isActive;
        this.title = title;
        this.summary = summary;
        this.country = country;
        this.city = city;
        this.imageUrl = imageUrl;
        this.goal = goal;
        this.funding = funding;
        this.remainingFunding = remainingFunding;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getGoal() {
        return goal;
    }

    public void setGoal(float goal) {
        this.goal = goal;
    }

    public float getFunding() {
        return funding;
    }

    public void setFunding(float funding) {
        this.funding = funding;
    }

    public float getRemainingFunding() {
        return remainingFunding;
    }

    public void setRemainingFunding(float remainingFunding) {
        this.remainingFunding = remainingFunding;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", isActive=" + isActive +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", goal=" + goal +
                ", funding=" + funding +
                ", remainingFunding=" + remainingFunding +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeByte(this.isActive ? (byte) 1 : (byte) 0);
        dest.writeString(this.title);
        dest.writeString(this.summary);
        dest.writeString(this.country);
        dest.writeString(this.city);
        dest.writeString(this.imageUrl);
        dest.writeFloat(this.goal);
        dest.writeFloat(this.funding);
        dest.writeFloat(this.remainingFunding);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.isActive = source.readByte() != 0;
        this.title = source.readString();
        this.summary = source.readString();
        this.country = source.readString();
        this.city = source.readString();
        this.imageUrl = source.readString();
        this.goal = source.readFloat();
        this.funding = source.readFloat();
        this.remainingFunding = source.readFloat();
    }

    protected Project(Parcel in) {
        this.id = in.readInt();
        this.isActive = in.readByte() != 0;
        this.title = in.readString();
        this.summary = in.readString();
        this.country = in.readString();
        this.city = in.readString();
        this.imageUrl = in.readString();
        this.goal = in.readFloat();
        this.funding = in.readFloat();
        this.remainingFunding = in.readFloat();
    }

    public static final Parcelable.Creator<Project> CREATOR = new Parcelable.Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel source) {
            return new Project(source);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };
}
