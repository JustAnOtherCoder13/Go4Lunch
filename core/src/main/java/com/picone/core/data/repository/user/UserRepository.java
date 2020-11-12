package com.picone.core.data.repository.user;

import com.picone.core.domain.entity.user.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class UserRepository {

    @Inject
    protected UserDaoImpl userDao;

    public UserRepository(UserDaoImpl dao) {
        userDao = dao;
    }

    public Observable<List<User>> getAllUsers() {
        return userDao.getAllUsers();
    }

    public Completable addUser(User user) {
        return userDao.AddUser(user);
    }

    public Completable updateUser(User currentUser) {
        return userDao.updateUser(currentUser);
    }

    public Observable<List<User>> getCurrentUserForEmail(String authUserEmail) {
        return userDao.getCurrentUserForEmail(authUserEmail);
    }

}
