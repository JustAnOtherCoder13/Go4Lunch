package com.picone.core.domain.interactors.usersInteractors;

import com.picone.core.data.repository.user.UserRepository;
import com.picone.core.domain.entity.UserDailySchedule;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetCurrentUserDailyScheduleOnTodayInteractor {

    @Inject
    UserRepository userDataSource;

    public GetCurrentUserDailyScheduleOnTodayInteractor(UserRepository userDataSource) {
        this.userDataSource = userDataSource;
    }

    public Observable<List<UserDailySchedule>> getCurrentUserDailyScheduleOnToday(String uId) {
        return userDataSource.getCurrentUserDailyScheduleOnToday(uId);
    }
}
