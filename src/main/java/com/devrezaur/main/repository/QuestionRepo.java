package com.devrezaur.main.repository;

import com.devrezaur.main.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepo extends JpaRepository<Question, Integer> {
    List<Question> findByQuizId(Long quizId);
}
