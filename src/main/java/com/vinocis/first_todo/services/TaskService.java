package com.vinocis.first_todo.services;

import com.vinocis.first_todo.models.Task;
import com.vinocis.first_todo.models.User;
import com.vinocis.first_todo.services.UserService;
import com.vinocis.first_todo.repositories.TaskRepository;
import com.vinocis.first_todo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserService userService;

    public Task findById(Integer id) {
        Optional<Task> user = this.taskRepository.findById(id);

        return user.orElseThrow(() -> new RuntimeException("Task not found. Id: " + id));
    }

    public Task create(Task task) {
        User user = this.userService.findById(task.getUser().getId());
        task.setId(null);
        task.setUser(user);

        return this.taskRepository.save(task);
    }

    public Task update(Task task) {
        Task newTask = findById(task.getId());
        newTask.setDescription(task.getDescription());

        return this.taskRepository.save(newTask);
    }

    public void delete(Integer id){
        findById(id);
        try {
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Can not delete. Entity has entities related to them");
        }
    }
}
