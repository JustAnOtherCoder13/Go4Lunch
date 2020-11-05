package com.picone.go4lunch;

import com.picone.core.data.Generator;

import org.junit.Test;

import java.util.Objects;

import static com.picone.core.data.ConstantParameter.SETTING_START_VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UserViewModelUnitTest extends BaseTest {

    @Test
    public void testNotNull() {
        assertNotNull(userViewModel);
        assertNotNull(userViewModel.getAllUsers().getValue());
        assertTrue(userViewModel.getAllUsers().hasObservers());
    }

    @Test
    public void getAllUsersShouldReturnGeneratedUsers() {
        assertEquals(3, Objects.requireNonNull(userViewModel.getAllUsers().getValue()).size());
        assertEquals(Generator.generateUsers().get(0).getName(), userViewModel.getAllUsers().getValue().get(0).getName());
    }

    @Test
    public void addUserShouldAddUserInList() {
        userViewModel.addUser(userToAdd);
        assertEquals(4, Objects.requireNonNull(userViewModel.getAllUsers().getValue()).size());
        assertEquals(userToAdd.getName(), userViewModel.getAllUsers().getValue().get(3).getName());
    }

    @Test
    public void updateUserSettingValueShouldUpdateUser(){
        assertNull(userToAdd.getSettingValues().getChosenLanguage());

        userViewModel.updateUserSettingValues(userToAdd,SETTING_START_VALUE);
        assertNotNull(userToAdd.getSettingValues().getChosenLanguage());
    }
}