package com.picone.go4lunch;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static com.picone.go4lunch.presentation.utils.ConstantParameter.SETTING_START_VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class UserViewModelUnitTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private UserViewModel userViewModel;

    private SchedulerProvider schedulerProvider = new SchedulerProvider(Schedulers.trampoline(), Schedulers.trampoline());
    private User user = new User("", "Marc", "", "", new ArrayList<>(), new SettingValues());

    private List<User> allUsers = new ArrayList<>();

    @Mock
    UserDaoImpl userDao;
    @Mock
    Observer<List<User>> userObserver;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserRepository userRepository = new UserRepository(userDao);
        GetAllUsersInteractor getAllUsersInteractor = new GetAllUsersInteractor(userRepository);
        AddUserInteractor addUserInteractor = new AddUserInteractor(userRepository);
        UpdateUserInteractor updateUserInteractor = new UpdateUserInteractor(userRepository);
        userViewModel = new UserViewModel(getAllUsersInteractor, addUserInteractor, updateUserInteractor, schedulerProvider);
        userViewModel.getAllUsers().observeForever(userObserver);

        allUsers.add(user);

        when(userDao.getAllUsers()).thenReturn(Observable.create(emitter -> emitter.onNext(allUsers)));
        when(userDao.AddUser(user)).thenReturn(Completable.create(emitter -> allUsers.add(user)));
        when(userDao.updateUser(user)).thenReturn(Completable.create(emitter -> user.setSettingValues(SETTING_START_VALUE)));

        userViewModel.setAllDbUsers();
    }

    @Test
    public void testNotNull() {
        assertNotNull(userViewModel);
        assertNotNull(userViewModel.getAllUsersInteractor);
        assertNotNull(userViewModel.addUserInteractor);
        assertNotNull(userViewModel.updateUserInteractor);
        assertNotNull(userViewModel.getAllUsers());
        assertNotNull(userViewModel.getAllUsers().getValue());
        assertTrue(userViewModel.getAllUsers().hasObservers());
    }

    @Test
    public void setGetAllUsersInteractor() {
        assertNotNull(userViewModel.getAllUsers().getValue());
        assertEquals("Marc", userViewModel.getAllUsers().getValue().get(0).getName());
    }

    @Test
    public void addUserShouldAddUserInList() {
        userViewModel.addUser(user);
        assertEquals(2, Objects.requireNonNull(userViewModel.getAllUsers().getValue()).size());
    }

    @Test
    public void updateUserSettingValueShouldUpdateUser(){
        assertNull(user.getSettingValues().getChosenLanguage());
        userViewModel.updateUserSettingValues(user,SETTING_START_VALUE);
        assertNotNull(user.getSettingValues().getChosenLanguage());
    }
}