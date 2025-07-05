package com.example.ElimuSmart.repository;

import com.example.ElimuSmart.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
