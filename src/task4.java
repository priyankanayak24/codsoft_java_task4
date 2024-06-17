import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

class Question {
    private String question;
    private String[] options;
    private int correctAnswerIndex;
    private int userAnswerIndex;

    public Question(String question, String[] options, int correctAnswerIndex) {
        this.question = question;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
        this.userAnswerIndex = -1; // Default to no answer
    }

    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public void setUserAnswerIndex(int index) {
        this.userAnswerIndex = index;
    }

    public int getUserAnswerIndex() {
        return userAnswerIndex;
    }
}

public class task4 extends JFrame {
    private static ArrayList<Question> mathsQuestions = new ArrayList<>();
    private static ArrayList<Question> historyQuestions = new ArrayList<>();
    private static ArrayList<Question> computerQuestions = new ArrayList<>();
    private static int currentQuestionIndex = 0;
    private static int score = 0;
    private JPanel mainPanel;
    private JLabel questionLabel;
    private JButton[] optionButtons;
    private Timer timer = new Timer();
    private Question currentQuestion;
    private TimerTask timerTask;
    private String userName;
    private JLabel timerLabel;
    private int timeLeft = 60; // 60 seconds for the entire quiz

    public task4() {
        initializeQuestions();
        setTitle("Quiz Application");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        askUserName();

        setVisible(true);
    }

    private void askUserName() {
        mainPanel.removeAll();

        JPanel namePanel = new JPanel(new GridBagLayout());
        namePanel.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;

        JLabel welcomeLabel = new JLabel("Welcome to the Quiz!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 36));
        namePanel.add(welcomeLabel, c);

        c.gridy++;
        JLabel nameLabel = new JLabel("Please enter your name:", SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        namePanel.add(nameLabel, c);

        c.gridy++;
        JTextField nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 24));
        namePanel.add(nameField, c);

        c.gridy++;
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 24));
        submitButton.setBackground(Color.YELLOW);
        submitButton.addActionListener(e -> {
            userName = nameField.getText();
            showSubjectChoice();
        });
        namePanel.add(submitButton, c);

        mainPanel.add(namePanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void showSubjectChoice() {
        mainPanel.removeAll();

        JPanel subjectPanel = new JPanel(new GridBagLayout());
        subjectPanel.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;

        JLabel welcomeLabel = new JLabel(userName + ", welcome to the Quiz! Choose which subject quiz you are interested in:", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        subjectPanel.add(welcomeLabel, c);

        c.gridy++;
        c.gridwidth = 1;

        JButton mathsButton = createSubjectButton("Maths");
        c.gridx = 0;
        subjectPanel.add(mathsButton, c);

        JButton historyButton = createSubjectButton("History");
        c.gridx = 1;
        subjectPanel.add(historyButton, c);

        JButton computerButton = createSubjectButton("Computer");
        c.gridx = 2;
        subjectPanel.add(computerButton, c);

        mathsButton.addActionListener(e -> startQuiz(mathsQuestions));
        historyButton.addActionListener(e -> startQuiz(historyQuestions));
        computerButton.addActionListener(e -> startQuiz(computerQuestions));

        mainPanel.add(subjectPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private JButton createSubjectButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.YELLOW);
        button.setPreferredSize(new Dimension(200, 50));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        return button;
    }

    private void startQuiz(ArrayList<Question> questions) {
        currentQuestionIndex = 0;
        score = 0;
        timeLeft = 60; // Reset the timer for the new quiz
        startTimer();
        displayQuestion(questions);
    }

    private void displayQuestion(ArrayList<Question> questions) {
        if (currentQuestionIndex < questions.size()) {
            currentQuestion = questions.get(currentQuestionIndex);

            mainPanel.removeAll();
            JPanel questionPanel = new JPanel(new GridBagLayout());
            questionPanel.setOpaque(false);

            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(10, 10, 10, 10);
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 2;
            c.anchor = GridBagConstraints.CENTER;

            questionLabel = new JLabel("<html><body style='text-align: center'>" + currentQuestion.getQuestion() + "</body></html>", SwingConstants.CENTER);
            questionLabel.setFont(new Font("Arial", Font.BOLD, 20));
            questionPanel.add(questionLabel, c);

            JPanel optionsPanel = new JPanel(new GridBagLayout());
            optionsPanel.setOpaque(false);
            c.gridy++;
            c.gridwidth = 1;
            c.gridx = 0;

            String[] labels = {"A", "B", "C", "D"};
            optionButtons = new JButton[currentQuestion.getOptions().length];
            for (int i = 0; i < currentQuestion.getOptions().length; i++) {
                optionButtons[i] = new JButton(labels[i] + ": " + currentQuestion.getOptions()[i]);
                optionButtons[i].setBackground(Color.YELLOW);
                int answerIndex = i;
                optionButtons[i].addActionListener(e -> submitAnswer(answerIndex, questions));
                c.gridx = i;
                optionsPanel.add(optionButtons[i], c);
            }
            questionPanel.add(optionsPanel, c);

            // Add the timer label to the top-right corner
            timerLabel = new JLabel("Time left: " + timeLeft + "s");
            timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
            mainPanel.add(timerLabel, BorderLayout.NORTH);

            mainPanel.add(questionPanel, BorderLayout.CENTER);

            revalidate();
            repaint();
        } else {
            showResult();
        }
    }

    private void startTimer() {
        if (timerTask != null) {
            timerTask.cancel();
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (timeLeft > 0) {
                    timeLeft--;
                    timerLabel.setText("Time left: " + timeLeft + "s");
                } else {
                    timer.cancel();
                    JOptionPane.showMessageDialog(mainPanel, "Time's up! Quiz over.");
                    showResult();
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    private void submitAnswer(int answer, ArrayList<Question> questions) {
        if (timeLeft > 0) {
            currentQuestion.setUserAnswerIndex(answer);
            if (answer == currentQuestion.getCorrectAnswerIndex()) {
                score++;
            }
            currentQuestionIndex++;
            if (questions != null) {
                displayQuestion(questions);
            }
        }
    }

    private void showResult() {
        mainPanel.removeAll();

        JPanel resultPanel = new JPanel(new GridBagLayout());
        resultPanel.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;

        JLabel resultLabel = new JLabel("Quiz finished! Your score: " + score + "/" + currentQuestionIndex, SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 24));
        resultPanel.add(resultLabel, c);

        c.gridy++;
        c.gridwidth = 1;

        JButton returnButton = new JButton("start Again");
        returnButton.setBackground(Color.YELLOW);
        returnButton.addActionListener(e -> showSubjectChoice());
        c.gridx = 0;
        resultPanel.add(returnButton, c);

        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(Color.YELLOW);
        exitButton.addActionListener(e -> System.exit(0));
        c.gridx = 1;
        resultPanel.add(exitButton, c);

        mainPanel.add(resultPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private void initializeQuestions() {
        mathsQuestions.add(new Question("What is 2+2?", new String[]{"3", "4", "5", "6"}, 1));
        mathsQuestions.add(new Question("What is 10/2?", new String[]{"3", "5", "2", "4"}, 1));
        mathsQuestions.add(new Question("What is 3*3?", new String[]{"6", "7", "8", "9"}, 3));
        mathsQuestions.add(new Question("What is 15-7?", new String[]{"5", "6", "7", "8"}, 2));

        historyQuestions.add(new Question("Who was the first president of the United States?", new String[]{"George Washington", "John Adams", "Thomas Jefferson", "James Madison"}, 0));
        historyQuestions.add(new Question("In which year did World War II end?", new String[]{"1942", "1944", "1945", "1946"}, 2));
        historyQuestions.add(new Question("Who discovered America?", new String[]{"Christopher Columbus", "Leif Erikson", "Amerigo Vespucci", "Ferdinand Magellan"}, 0));
        historyQuestions.add(new Question("What was the name of the ship that brought the Pilgrims to America?", new String[]{"Santa Maria", "Mayflower", "Pinta", "Nina"}, 1));

        computerQuestions.add(new Question("What does CPU stand for?", new String[]{"Central Process Unit", "Central Processing Unit", "Computer Personal Unit", "Central Processor Unit"}, 1));
        computerQuestions.add(new Question("What is the brain of the computer?", new String[]{"RAM", "Motherboard", "CPU", "Hard Drive"}, 2));
        computerQuestions.add(new Question("What does RAM stand for?", new String[]{"Random Access Memory", "Read Access Memory", "Run Access Memory", "Real Access Memory"}, 0));
        computerQuestions.add(new Question("What is the main circuit board of the computer?", new String[]{"RAM", "Motherboard", "CPU", "Hard Drive"}, 1));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(task4::new);
    }
}

class GradientPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();
        Color color1 = Color.BLUE;
        Color color2 = Color.CYAN;
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);
    }
}
