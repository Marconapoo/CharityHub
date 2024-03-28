package it.sal.disco.unimib.charityhub.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImagesApiResponse {

    @SerializedName("images")
    private ImagesObject imagesObject;

    public ImagesObject getImagesObject() {
        return imagesObject;
    }

    public static class ImagesObject {
        @SerializedName("image")
        private List<ImagesArray> imagesArrays;

        public List<ImagesArray> getImagesArrays() {
            return imagesArrays;
        }
    }

    public static class ImagesArray {
        @SerializedName("imagelink")
        private List<Image> imageLinks;

        public List<Image> getImageLinks() {
            return imageLinks;
        }
    }

}
