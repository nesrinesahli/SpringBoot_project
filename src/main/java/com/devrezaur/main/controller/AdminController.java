@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private QuizRepo quizRepo;

    @Autowired
    private QuestionRepo questionRepo;

    // Load Admin Dashboard
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("quizzes", quizRepo.findAll());
        return "admin-dashboard";
    }

    // Add New Category
    @PostMapping("/category/add")
    public String addCategory(@RequestParam("name") String name) {
        Category category = new Category();
        category.setName(name);
        categoryRepo.save(category);
        return "redirect:/admin/dashboard";
    }

    // Add New Quiz
    @PostMapping("/quiz/add")
    public String addQuiz(@RequestParam("title") String title, @RequestParam("categoryId") Long categoryId) {
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
        quiz.setCategory(category);
        quizRepo.save(quiz);
        return "redirect:/admin/dashboard";
    }

    // Add New Question
    @PostMapping("/question/add")
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("optionA") String optionA,
                              @RequestParam("optionB") String optionB,
                              @RequestParam("optionC") String optionC,
                              @RequestParam("correctAnswer") int correctAnswer,
                              @RequestParam("quizId") Long quizId) {
        Question question = new Question();
        question.setTitle(title);
        question.setOptionA(optionA);
        question.setOptionB(optionB);
        question.setOptionC(optionC);
        question.setAns(correctAnswer);
        Quiz quiz = quizRepo.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));
        question.setQuiz(quiz);
        questionRepo.save(question);
        return "redirect:/admin/dashboard";
    }
}
