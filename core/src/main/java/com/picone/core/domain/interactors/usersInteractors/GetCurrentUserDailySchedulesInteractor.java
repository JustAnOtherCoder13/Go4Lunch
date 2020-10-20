package com.picone.core.domain.interactors.usersInteractors;

import com.picone.core.data.repository.user.UserRepository;
import com.picone.core.domain.entity.user.UserDailySchedule;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetCurrentUserDailySchedulesInteractor {

    @Inject
    UserRepository userDataSource;

    public GetCurrentUserDailySchedulesInteractor(UserRepository userDataSource) {
        this.userDataSource = userDataSource;
    }

    public Observable<List<UserDailySchedule>> getCurrentUserDailySchedules(String uId) {
        return userDataSource.getCurrentUserDailySchedules(uId);
    }
}
