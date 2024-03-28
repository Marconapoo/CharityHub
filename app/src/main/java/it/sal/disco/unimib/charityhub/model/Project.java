package it.sal.disco.unimib.charityhub.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Project implements Parcelable {


    private int id;
    private String title;
    private String summary;
    @SerializedName("need")
    private String challenge;
    private String longTermImpact;
    @SerializedName("activities")
    private String solution;
    @SerializedName("imageLink")
    private String imageUrl;
    private float goal;
    private float funding;
    @SerializedName("remaining")
    private float remainingFunding;
    private int numberOfDonations;

    public Project(int id, String title, String summary, String challenge, String longTermImpact, String solution, String imageUrl, float goal, float funding, float remainingFunding, int numberOfDonations) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.challenge = challenge;
        this.longTermImpact = longTermImpact;
        this.solution = solution;
        this.imageUrl = imageUrl;
        this.goal = goal;
        this.funding = funding;
        this.remainingFunding = remainingFunding;
        this.numberOfDonations = numberOfDonations;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    public String getLongTermImpact() {
        return longTermImpact;
    }

    public void setLongTermImpact(String longTermImpact) {
        this.longTermImpact = longTermImpact;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
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

    public int getNumberOfDonations() {
        return numberOfDonations;
    }

    public void setNumberOfDonations(int numberOfDonations) {
        this.numberOfDonations = numberOfDonations;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", challenge='" + challenge + '\'' +
                ", longTermImpact='" + longTermImpact + '\'' +
                ", solution='" + solution + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", goal=" + goal +
                ", funding=" + funding +
                ", remainingFunding=" + remainingFunding +
                ", numberOfDonations=" + numberOfDonations +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.summary);
        dest.writeString(this.challenge);
        dest.writeString(this.longTermImpact);
        dest.writeString(this.solution);
        dest.writeString(this.imageUrl);
        dest.writeFloat(this.goal);
        dest.writeFloat(this.funding);
        dest.writeFloat(this.remainingFunding);
        dest.writeInt(this.numberOfDonations);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.title = source.readString();
        this.summary = source.readString();
        this.challenge = source.readString();
        this.longTermImpact = source.readString();
        this.solution = source.readString();
        this.imageUrl = source.readString();
        this.goal = source.readFloat();
        this.funding = source.readFloat();
        this.remainingFunding = source.readFloat();
        this.numberOfDonations = source.readInt();
    }

    protected Project(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.summary = in.readString();
        this.challenge = in.readString();
        this.longTermImpact = in.readString();
        this.solution = in.readString();
        this.imageUrl = in.readString();
        this.goal = in.readFloat();
        this.funding = in.readFloat();
        this.remainingFunding = in.readFloat();
        this.numberOfDonations = in.readInt();
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
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
