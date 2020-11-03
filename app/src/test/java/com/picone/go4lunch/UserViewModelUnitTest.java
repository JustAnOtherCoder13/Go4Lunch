package com.picone.go4lunch;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.data.repository.user.UserDaoImpl;
import com.picone.core.data.repository.user.UserRepository;
import com.picone.core.domain.entity.user.SettingValues;
import com.picone.core.domain.entity.user.User;
import com.picone.core.domain.interactors.usersInteractors.AddUserInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetAllUsersInteractor;
import com.picone.core.domain.interactors.usersInteractors.UpdateUserInteractor;
import com.picone.go4lunch.presentation.utils.SchedulerProvider;
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
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class UserViewModelUnitTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    UserViewModel userViewModel;

    @Mock
    AddUserInteractor addUserInteractor;
    @Mock
    UpdateUserInteractor updateUserInteractor;

    @Mock
    Observer<List<User>> userObserver;

    UserRepository userRepository;

    @Mock
    UserDaoImpl userDao;

    SchedulerProvider schedulerProvider = new SchedulerProvider(Schedulers.trampoline(), Schedulers.trampoline());


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        userRepository = new UserRepository(userDao);
        GetAllUsersInteractor getAllUsersInteractor = new GetAllUsersInteractor(userRepository);
        userViewModel = new UserViewModel(getAllUsersInteractor, addUserInteractor, updateUserInteractor, schedulerProvider);
        userViewModel.getAllUsers().observeForever(userObserver);
    }

    @Test
    public void testNotNull() {
        assertNotNull(userViewModel);
        assertNotNull(userViewModel.getAllUsersInteractor);
        assertNotNull(userViewModel.addUserInteractor);
        assertNotNull(userViewModel.updateUserInteractor);
        assertNotNull(userViewModel.getAllUsers());
        assertTrue(userViewModel.getAllUsers().hasObservers());
    }

    @Test
    public void setGetAllUsersInteractor() {

        User user = new User("", "Marc", "", "", new ArrayList<>(), new SettingValues());
        List<User> allUsers = new ArrayList<>();
        allUsers.add(user);
        when(userViewModel.getAllUsersInteractor.getAllUsers()).thenReturn(Observable.create(emitter -> emitter.onNext(allUsers)));
        userViewModel.setAllDbUsers();

        assertNotNull(userViewModel.getAllUsers().getValue());
        assertEquals("Marc",userViewModel.getAllUsers().getValue().get(0).getName());
        }
}