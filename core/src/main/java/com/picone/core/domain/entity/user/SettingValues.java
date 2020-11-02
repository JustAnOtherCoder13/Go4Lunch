package com.picone.core.domain.entity.user;

public class SettingValues {

    private String chosenLanguage;
    private boolean isNotificationSet;

    @SuppressWarnings("unused")
    public SettingValues() {
    }

    public SettingValues(String chosenLanguage, boolean isNotificationSet) {
        this.chosenLanguage = chosenLanguage;
        this.isNotificationSet = isNotificationSet;
    }

    public String getChosenLanguage() {
        return chosenLanguage;
    }

    public boolean isNotificationSet() {
        return isNotificationSet;
    }
}
