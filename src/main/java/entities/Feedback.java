package entities;

public class Feedback {

    private int id;
    private Cour course;  // Linked to Cour
    private String content;

    public Feedback() {
    }

    public Feedback(Cour course, String content) {
        this.course = course;
        this.content = content;
    }

    public Feedback(int id, Cour course, String content) {
        this.id = id;
        this.course = course;
        this.content = content;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Cour getCourse() { return course; }
    public void setCourse(Cour course) { this.course = course; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
} 