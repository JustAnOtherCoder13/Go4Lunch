package com.picone.core.domain.entity.user;

public class SettingValues {

    String chosenLanguage;
    boolean isNotificationSet;

    public SettingValues() {
    }

    public SettingValues(String chosenLanguage, boolean isNotificationSet) {
        this.chosenLanguage = chosenLanguage;
        this.isNotificationSet = isNotificationSet;
    }

    public String getChosenLanguage() {
        return chosenLanguage;
    }

    public void setChosenLanguage(String chosenLanguage) {
        this.chosenLanguage = chosenLanguage;
    }

    public boolean isNotificationSet() {
        return isNotificationSet;
    }

    public void setNotificationSet(boolean notificationSet) {
        isNotificationSet = notificationSet;
    }
}
