package com.picone.core.data.repository;

import com.picone.core.domain.entity.User;

import java.util.List;

public interface UserDao {

    List<User> getAllUsers();

    User getUser(int position);

    void AddUser(User user);
}
