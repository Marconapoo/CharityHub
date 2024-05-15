package it.sal.disco.unimib.charityhub.model.projects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ThemesApiResponse {

    @SerializedName("themes")
    private ThemeData themeData;
    public ThemesApiResponse(List<Theme> themes) {
        themeData = new ThemeData(themes);
    }


    public ThemeData getThemeData() {
        return themeData;
    }
    public static class ThemeData {
        private List<Theme> theme;

        public ThemeData(List<Theme> themes) {
            this.theme = themes;
        }

        public List<Theme> getThemes() {
            return theme;
        }

        public void setTheme(List<Theme> theme) {
            this.theme = theme;
        }
    }

}
