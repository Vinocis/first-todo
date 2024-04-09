package com.vinocis.first_todo.services;

import com.vinocis.first_todo.models.User;
import com.vinocis.first_todo.repositories.TaskRepository;
import com.vinocis.first_todo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.RuntimeErrorException;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;

    public User findById(Integer id) {
        Optional<User> user = this.userRepository.findById(id);

        return user.orElseThrow(() -> new RuntimeException("User not found. Id: " + id));
    }

    @Transactional
    public User create(User user) {
        user.setId(null);
        user = this.userRepository.save(user);
        this.taskRepository.saveAll(user.getTasks());
        return user;
    }

    public User update(User user) {
        User newUser = findById(user.getId());
        newUser.setPassword(user.getPassword());
        return this.userRepository.save(newUser);
    }

    public void delete(Integer id) {
        findById(id);

        try {
        this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Can not delete. Entity has entities related to them");
        }
    }
}
