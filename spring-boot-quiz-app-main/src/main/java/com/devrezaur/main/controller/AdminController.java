package com.devrezaur.main.controller;

import com.devrezaur.main.model.Category;
import com.devrezaur.main.model.Question;
import com.devrezaur.main.model.Quiz;
import com.devrezaur.main.repository.CategoryRepo;
import com.devrezaur.main.repository.QuestionRepo;
import com.devrezaur.main.repository.QuizRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private QuizRepo quizRepo;

    @Autowired
    private QuestionRepo questionRepo;

    // Admin Dashboard
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        return "admin-dashboard";
    }

    // Add New Category
    @PostMapping("/admin/category/add")
    public String addCategory(@RequestParam String categoryName, Model model) {
        Category category = new Category();
        category.setName(categoryName);
        categoryRepo.save(category);

        return "redirect:/admin-dashboard";
    }

    // Add New Quiz to a Category
    @GetMapping("/admin/category/{categoryId}/quizzes")
    public String manageQuizzes(@PathVariable Long categoryId, Model model) {
        List<Quiz> quizzes = quizRepo.findByCategoryId(categoryId);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("quizzes", quizzes);
        return "manage-quizzes"; // manage-quizzes.html
    }

    @PostMapping("/admin/category/{categoryId}/quiz/add")
    public String addQuiz(@PathVariable Long categoryId, @RequestParam String quizTitle, Model model) {
        Quiz quiz = new Quiz();
        quiz.setTitle(quizTitle);
        quizRepo.save(quiz);

        return "redirect:/admin/category/" + categoryId + "/quizzes";
    }

    // Add Questions to a Quiz
    @GetMapping("/admin/quiz/{quizId}/questions")
    public String manageQuestions(@PathVariable Long quizId, Model model) {
        List<Question> questions = questionRepo.findByQuizId(quizId);
        model.addAttribute("quizId", quizId);
        model.addAttribute("questions", questions);
        return "manage-questions"; // manage-questions.html
    }

    @PostMapping("/admin/quiz/{quizId}/question/add")
    public String addQuestion(@PathVariable Long quizId,
                              @RequestParam String title,
                              @RequestParam String optionA,
                              @RequestParam String optionB,
                              @RequestParam String optionC,
                              @RequestParam int correctOption,
                              Model model) {

        Question question = new Question();
        question.setTitle(title);
        question.setOptionA(optionA);
        question.setOptionB(optionB);
        question.setOptionC(optionC);
        question.setAns(correctOption);
        questionRepo.save(question);

        return "redirect:/admin/quiz/" + quizId + "/questions";
    }
}
