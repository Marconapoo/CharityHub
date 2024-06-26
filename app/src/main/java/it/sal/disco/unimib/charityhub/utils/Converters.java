package it.sal.disco.unimib.charityhub.utils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import it.sal.disco.unimib.charityhub.model.projects.DonationOptions;

public class Converters {
    @TypeConverter
    public static DonationOptions fromString(String value) {
        return new Gson().fromJson(value, DonationOptions.class);
    }

    @TypeConverter
    public static String donationOptionsToString(DonationOptions donationOptions) {
        return new Gson().toJson(donationOptions);
    }
}

