package it.sal.disco.unimib.charityhub.model.projects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DonationOptions implements Parcelable {

    @SerializedName("donationOption")
    private List<DonationOption> donationOptionList;

    public List<DonationOption> getDonationOptionList() {
        return donationOptionList;
    }

    public static class DonationOption implements Parcelable {
        private int amount;
        private String description;

        public int getAmount() {
            return amount;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.amount);
            dest.writeString(this.description);
        }

        public void readFromParcel(Parcel source) {
            this.amount = source.readInt();
            this.description = source.readString();
        }

        public DonationOption() {
        }

        protected DonationOption(Parcel in) {
            this.amount = in.readInt();
            this.description = in.readString();
        }

        public static final Creator<DonationOption> CREATOR = new Creator<DonationOption>() {
            @Override
            public DonationOption createFromParcel(Parcel source) {
                return new DonationOption(source);
            }

            @Override
            public DonationOption[] newArray(int size) {
                return new DonationOption[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.donationOptionList);
    }

    public void readFromParcel(Parcel source) {
        this.donationOptionList = new ArrayList<DonationOption>();
        source.readList(this.donationOptionList, DonationOption.class.getClassLoader());
    }

    public DonationOptions() {
    }

    protected DonationOptions(Parcel in) {
        this.donationOptionList = new ArrayList<DonationOption>();
        in.readList(this.donationOptionList, DonationOption.class.getClassLoader());
    }

    public static final Parcelable.Creator<DonationOptions> CREATOR = new Parcelable.Creator<DonationOptions>() {
        @Override
        public DonationOptions createFromParcel(Parcel source) {
            return new DonationOptions(source);
        }

        @Override
        public DonationOptions[] newArray(int size) {
            return new DonationOptions[size];
        }
    };
}
