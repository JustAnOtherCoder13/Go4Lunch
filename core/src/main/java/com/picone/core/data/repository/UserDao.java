package com.picone.core.data.repository;

import com.picone.core.domain.entity.User;

import java.util.List;

import io.reactivex.Observable;

public interface UserDao {

    Observable<List<User>> getAllUsers();

    User getUser(int position);

    void AddUser(User user);
}
