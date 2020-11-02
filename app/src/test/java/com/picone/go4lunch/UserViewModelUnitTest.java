package com.picone.go4lunch;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.picone.core.domain.entity.user.SettingValues;
import com.picone.core.domain.entity.user.User;
import com.picone.core.domain.interactors.usersInteractors.AddUserInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetAllUsersInteractor;
import com.picone.core.domain.interactors.usersInteractors.UpdateUserInteractor;
import com.picone.go4lunch.presentation.ui.main.MainActivity;
import com.picone.go4lunch.presentation.viewModels.UserViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;

public class UserViewModelUnitTest {

    //Todo test live data not interactors

    //Todo helper for rx

    //Todo viewModel

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    /*@Rule
    public ImmediateRxSchedulersOverrideRule immediateRxSchedulersOverrideRule = new ImmediateRxSchedulersOverrideRule();*/

    @Mock
    AddUserInteractor addUserInteractor;
    @Mock
    UpdateUserInteractor updateUserInteractor;

    UserViewModel userViewModel;
    @Mock
    Observer<List<User>> userObserver;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GetAllUsersInteractor getAllUsersInteractor = Mockito.mock(GetAllUsersInteractor.class);
        UserViewModel userViewModel = new UserViewModel(getAllUsersInteractor, addUserInteractor, updateUserInteractor);
        userViewModel.setAllDbUsers();
        userViewModel.getAllUsers.observeForever(userObserver);
    }


    @Test
    public void setGetAllUsersInteractor() {
        assertNotNull(userViewModel.getAllUsers.getValue());
        /*User user = new User("", "Marc", "", "", new ArrayList<>(), new SettingValues());
        userViewModel.getAllUsers.getValue().add(user);

        List<User> allUsers2 = new ArrayList<>();

        allUsers2.add(user);

        verify(userObserver).onChanged(allUsers2);*/
    }
}