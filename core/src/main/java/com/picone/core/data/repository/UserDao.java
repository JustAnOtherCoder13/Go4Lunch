package com.picone.core.data.repository;

import com.picone.core.domain.entity.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface UserDao {

    Observable<List<User>> getAllUsers();

    Completable AddUser(User currentUser);
}
