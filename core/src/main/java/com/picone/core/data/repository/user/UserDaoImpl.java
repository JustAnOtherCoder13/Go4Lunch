package com.picone.core.data.repository.user;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.picone.core.domain.entity.user.User;

import java.util.List;

import javax.inject.Inject;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.Observable;

import static com.picone.core.utils.ConstantParameter.MAIL_REF;
import static com.picone.core.utils.ConstantParameter.USER_REF;

public class UserDaoImpl implements UserDao {

    @Inject
    protected FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersDatabaseReference;

    public UserDaoImpl(@NonNull FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
        this.usersDatabaseReference = firebaseDatabase.getReference().child(USER_REF);
    }

    @Override
    public Observable<List<User>> getAllUsers() {
        return RxFirebaseDatabase.observeValueEvent(usersDatabaseReference
                , DataSnapshotMapper.listOf(User.class)).toObservable();
    }

    @Override
    public Completable AddUser(@NonNull User user) {
        user.setUid(usersDatabaseReference.push().getKey());
        return RxFirebaseDatabase.setValue(usersDatabaseReference.child(user.getUid()), user);
    }

    @Override
    public Completable updateUser(User currentUser) {
        return RxFirebaseDatabase.setValue(usersDatabaseReference.child(currentUser.getUid()), currentUser);
    }

    @Override
    public Observable<List<User>> getCurrentUserForEmail(String authUserEmail) {
        Query query = usersDatabaseReference.orderByChild(MAIL_REF).equalTo(authUserEmail);
        return RxFirebaseDatabase.observeValueEvent(query, DataSnapshotMapper.listOf(User.class)).toObservable();
    }
}


