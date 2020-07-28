package com.picone.core.domain.interactors;

import com.picone.core.data.repository.UserRepository;
import com.picone.core.domain.entity.User;

import java.util.List;

public class GetAllUsers {
    public List<User> mUsers;

    public GetAllUsers(UserRepository dataSource){
        //mUsers = dataSource
    }
}
