package com.picone.core.data.repository.user;

import com.picone.core.domain.entity.user.User;
import com.picone.core.domain.entity.user.UserDailySchedule;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface UserDao {

    Observable<List<User>> getAllUsers();

    Completable AddUser(User user);

    Observable<List<User>> getCurrentUserForEmail(String authUserEmail);
}
