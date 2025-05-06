package entities;

public class Question {
    private int id;
    private int testId;
    private String enonce;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private int correctAnswer; // (1, 2, 3 ou 4)

    public Question() {
    }

    public Question(int testId, String enonce, String answer1, String answer2,
                    String answer3, String answer4, int correctAnswer) {
        this.testId = testId;
        this.enonce = enonce;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.correctAnswer = correctAnswer;
    }

    public Question(int id, int testId, String enonce, String answer1, String answer2,
                    String answer3, String answer4, int correctAnswer) {
        this.id = id;
        this.testId = testId;
        this.enonce = enonce;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.correctAnswer = correctAnswer;
    }

    public int getId() {
        return id;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public String getEnonce() {
        return enonce;
    }

    public void setEnonce(String enonce) {
        this.enonce = enonce;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
