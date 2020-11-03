package com.picone.go4lunch;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.picone.core.data.repository.user.UserDaoImpl;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserViewModelUnitTest {

    //Todo test live data not interactors

    //Todo helper for rx

    //Todo viewModel

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Rule public MockitoRule rule = MockitoJUnit.rule();
    /*@Rule
    public ImmediateRxSchedulersOverrideRule immediateRxSchedulersOverrideRule = new ImmediateRxSchedulersOverrideRule();*/

    @Mock
    AddUserInteractor addUserInteractor;
    @Mock
    UpdateUserInteractor updateUserInteractor;

    @Mock
    GetAllUsersInteractor getAllUsersInteractor;

    @Mock
    UserDaoImpl userDao;

    @InjectMocks
    UserViewModel userViewModel;

    @Mock
    Observer<List<User>> userObserver;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GetAllUsersInteractor getAllUsersInteractor = Mockito.mock(GetAllUsersInteractor.class);
        AddUserInteractor addUserInteractor = Mockito.mock(AddUserInteractor.class);
        UpdateUserInteractor updateUserInteractor = Mockito.mock(UpdateUserInteractor.class);
        userViewModel = new UserViewModel(getAllUsersInteractor, addUserInteractor, updateUserInteractor);
        userViewModel.getAllUsers.observeForever(userObserver);
    }

    @Test
    public void testNotNull(){
        assertNotNull(userViewModel);
        assertNotNull(userViewModel.getAllUsersInteractor);
        assertNotNull(userViewModel.addUserInteractor);
        assertNotNull(userViewModel.updateUserInteractor);
        assertNotNull(userViewModel.getAllUsers);
        assertTrue(userViewModel.getAllUsers.hasObservers());
    }
    @Test
    public void setGetAllUsersInteractor() {

       User user = new User("", "Marc", "", "", new ArrayList<>(), new SettingValues());
        List<User> allUsers2 = new ArrayList<>();
        allUsers2.add(user);

       when(userDao.getAllUsers()).thenReturn(Observable.just(
                allUsers2
        ));


       userViewModel.setAllDbUsers();

        /* userViewModel.getAllUsers.getValue().add(user);

        List<User> allUsers2 = new ArrayList<>();

        allUsers2.add(user);

        verify(userObserver).onChanged(allUsers2);*/
    }
}