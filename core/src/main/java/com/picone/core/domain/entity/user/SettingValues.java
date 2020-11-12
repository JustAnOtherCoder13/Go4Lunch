package com.picone.core.domain.entity.user;

public class SettingValues {

    private String chosenLanguage;
    private boolean notificationSet;

    @SuppressWarnings("unused")
    public SettingValues() {
    }

    public SettingValues(String chosenLanguage, boolean notificationSet) {
        this.chosenLanguage = chosenLanguage;
        this.notificationSet = notificationSet;
    }

    public String getChosenLanguage() {
        return chosenLanguage;
    }

    public boolean isNotificationSet() {
        return notificationSet;
    }
}
