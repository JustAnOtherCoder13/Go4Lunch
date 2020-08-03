package com.picone.core_.data.repository;

import com.picone.core_.domain.entity.User;

import java.util.List;

public interface UserDao {

    List<User> getAllUsers();

    User getUser();
}
