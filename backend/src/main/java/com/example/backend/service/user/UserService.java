package com.example.backend.service.user;

import com.example.backend.model.entities.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getById(Integer id);

    User create(User user);

    User update(User user);

    void delete(Integer id);

    List<User> getByLogin(String query);


}
