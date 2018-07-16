package com.hrtgo.heritagego.heritagego.Model;

public class modelLanguage {
    String languageName, languageCode;
    private static String languagePrefs = "Language";

    public static String getLanguagePrefs() {
        return languagePrefs;
    }

    public modelLanguage(String languageName, String languageCode) {
        this.languageName = languageName;
        this.languageCode = languageCode;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
}
