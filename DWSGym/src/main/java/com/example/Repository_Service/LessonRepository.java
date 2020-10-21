package com.example.Repository_Service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Lesson.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Long>{
}
