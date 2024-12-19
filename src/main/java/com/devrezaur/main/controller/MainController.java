package com.devrezaur.main.controller;

import com.devrezaur.main.model.QuestionForm;
import com.devrezaur.main.model.Quiz;
import com.devrezaur.main.model.Result;
import com.devrezaur.main.model.User;
import com.devrezaur.main.repository.CategoryRepo;
import com.devrezaur.main.repository.QuestionRepo;
import com.devrezaur.main.repository.QuizRepo;
import com.devrezaur.main.repository.ResultRepo;
import com.devrezaur.main.repository.UserRepo;
import com.devrezaur.main.service.QuizService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.devrezaur.main.model.Category;
import com.devrezaur.main.model.Question;
import java.util.List;



@Controller
public class MainController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private QuizService qService;

    private boolean submitted = false;
    private Result result;

	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private QuizRepo quizRepo;
	@Autowired
	private ResultRepo resultRepo;


	@Autowired
	private QuestionRepo questionRepo;
    // Home Page
    @GetMapping("/")
    public String home() {
        return "index.html";
    }

    // Admin Login Page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // Handle Login for Admin
    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, Model model) {
        User user = userRepo.findByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                return "redirect:/admin-dashboard";
            } else {
                model.addAttribute("username", user.getEmail());
                return "redirect:/";
            }
        } else {
            model.addAttribute("error", "Invalid email or password!");
            return "login";
        }
    }

    // Admin Dashboard
    @GetMapping("/admin-dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("message", "Welcome to the Admin Dashboard!");
        return "admin-dashboard";
    }

    // Handle User Login and Start Quiz
    @PostMapping("/user-login")
    public String userLogin(@RequestParam String email, @RequestParam String password, Model model) {
        // Verify user credentials
        User user = userRepo.findByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            // Valid user, start the quiz
            model.addAttribute("username", user.getEmail()); // Use email as username

            submitted = false; // Reset quiz submission status
            result = new Result();
            result.setUsername(email);

            // Load quiz questions
            QuestionForm qForm = qService.getQuestions();
            model.addAttribute("qForm", qForm);

            return "redirect:/categories"; // Redirect to quiz page
        } else {
            // Invalid credentials
            model.addAttribute("error", "Invalid email or password!");
            return "index"; // Back to home page with error
        }
    }
	@GetMapping("/register")
	public String showRegisterPage() {
		return "register"; 
	}
	@PostMapping("/register")
	public String registerUser(@RequestParam String email, 
							@RequestParam String password, 
							@RequestParam String confirmPassword, 
							Model model) {
		// Validate passwords
		if (!password.equals(confirmPassword)) {
			model.addAttribute("error", "Passwords do not match!");
			return "register";
		}

		// Check if user already exists
		if (userRepo.findByEmail(email) != null) {
			model.addAttribute("error", "Email already registered!");
			return "register";
		}

		// Create new user
		User newUser = new User();
		newUser.setEmail(email);
		newUser.setPassword(password);
		newUser.setRole("USER"); // Default role for new users

		userRepo.save(newUser);

		model.addAttribute("success", "Registration successful! Please login.");
		return "login";
	}
	@GetMapping("/categories")
	public String showCategories(Model model) {
		List<Category> categories = categoryRepo.findAll();
		System.out.println(categories);
		model.addAttribute("categories", categories);
		return "categories"; // This maps to categories.html
	}
	@GetMapping("/quizzes/{categoryId}")
	public String getQuizzesByCategory(@PathVariable Long categoryId, Model model) {
		List<Quiz> quizzes = quizRepo.findByCategoryId(categoryId);
		model.addAttribute("quizzes", quizzes);
		return "quizzes"; // Return the quizzes.html page
	}
	@GetMapping("/quiz/start/{quizId}")
	public String startQuiz(@PathVariable Long quizId, Model model) {
		QuestionForm qForm = new QuestionForm();
		qForm.setQuestions(questionRepo.findByQuizId(quizId));
		model.addAttribute("qForm", qForm); // Pass qForm to the view
		return "quiz"; // quiz.html
	}

	@PostMapping("/quiz/submit")
	public String submitQuiz(@ModelAttribute("qForm") QuestionForm qForm, Model model) {
		// Assume `userId` is obtained from session or input
		int userId = 1; // Replace with actual logic for logged-in user
		
		User user = userRepo.findById((long) userId).orElseThrow(() -> new RuntimeException("User not found"));
		
		int totalCorrect = calculateCorrectAnswers(qForm); // Implement this method
		
		Result result = new Result();
		result.setUser(user);
		result.setTotalCorrect(totalCorrect);
	
		resultRepo.save(result);
	
		model.addAttribute("result", result);
		return "result";
	}
	private int calculateCorrectAnswers(QuestionForm questionForm) {
		int correctCount = 0;
		for (Question question : questionForm.getQuestions()) {
			if (question.getChose() == question.getAns()) {
				correctCount++;
			}
		}
		return correctCount;
	}
	


}
