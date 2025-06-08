package com.example.backend.service.user;

import com.example.backend.model.entities.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.validation.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(Integer id) {
        return getOrElseThrow(id);
    }

    private User getOrElseThrow(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("User with id %s does not exist", id)));
    }

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        getOrElseThrow(user.getId());
        return userRepository.save(user);
    }

    @Override
    public void delete(Integer id) {
        getOrElseThrow(id);
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getByLogin(String query) {
        return null;
    }
}

