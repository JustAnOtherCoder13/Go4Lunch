package com.picone.go4lunch;

import androidx.lifecycle.Observer;

import com.picone.core.data.Generator;
import com.picone.core.domain.entity.user.User;
import com.picone.go4lunch.presentation.viewModels.UserViewModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Completable;
import io.reactivex.Observable;

import static com.picone.core.data.ConstantParameter.SETTING_START_VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class UserViewModelUnitTest extends BaseTest {

    private List<User> allUsers = new ArrayList<>();
    @Mock
    Observer<List<User>> userObserver;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        userViewModel = new UserViewModel(getAllUsersInteractor, addUserInteractor, updateUserInteractor, schedulerProvider);
        userViewModel.getAllUsers().observeForever(userObserver);

        allUsers.addAll(Generator.generateUsers());

        when(userRepository.getAllUsers()).thenReturn(Observable.create(emitter -> emitter.onNext(allUsers)));
        when(userRepository.addUser(userToAdd)).thenReturn(Completable.create(emitter -> Objects.requireNonNull(userViewModel.getAllUsers().getValue()).add(userToAdd)));
        when(userRepository.updateUser(userToAdd)).thenReturn(Completable.create(emitter -> {
            if (Objects.requireNonNull(userViewModel.getAllUsers().getValue()).contains(userToAdd))
            userToAdd.setSettingValues(SETTING_START_VALUE);}));

        userViewModel.setAllDbUsers();
    }

    @Test
    public void testNotNull() {
        assertNotNull(userViewModel);
        assertNotNull(userViewModel.getAllUsers().getValue());
        assertTrue(userViewModel.getAllUsers().hasObservers());
    }

    @Test
    public void getAllUsersShouldReturnGeneratedUsers() {
        assertEquals(3, Objects.requireNonNull(userViewModel.getAllUsers().getValue()).size());
        assertEquals("Jiji", userViewModel.getAllUsers().getValue().get(0).getName());
    }

    @Test
    public void addUserShouldAddUserInList() {
        userViewModel.addUser(userToAdd);
        assertEquals(4, Objects.requireNonNull(userViewModel.getAllUsers().getValue()).size());
        assertEquals("Marc", userViewModel.getAllUsers().getValue().get(3).getName());
    }

    @Test
    public void updateUserSettingValueShouldUpdateUser(){
        assertNull(userToAdd.getSettingValues().getChosenLanguage());

        userViewModel.updateUserSettingValues(userToAdd,SETTING_START_VALUE);
        assertNotNull(userToAdd.getSettingValues().getChosenLanguage());
    }
}