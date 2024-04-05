package it.sal.disco.unimib.charityhub.model.projects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ThemesApiResponse {

    @SerializedName("themes")
    private ThemeData themeData;

    public ThemeData getThemeData() {
        return themeData;
    }
    public static class ThemeData {
        private List<Theme> theme;

        public List<Theme> getThemes() {
            return theme;
        }
    }

}
