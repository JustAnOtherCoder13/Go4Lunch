package com.picone.core.data.repository;

import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface UserDao {

    Observable<List<User>> getAllUsers();

    User getUser(int position);

    Completable AddUser(User user);

    Observable<List<User>> interestedColleague();

    Completable updateInterestedColleague(User user);
}
