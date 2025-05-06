package entities;

public class Rating {

    private int id;
    private Cour course;
    private int ratingValue; // 1 to 5 stars

    public Rating() {
    }

    public Rating(Cour course, int ratingValue) {
        this.course = course;
        this.ratingValue = ratingValue;
    }

    public Rating(int id, Cour course, int ratingValue) {
        this.id = id;
        this.course = course;
        this.ratingValue = ratingValue;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Cour getCourse() { return course; }
    public void setCourse(Cour course) { this.course = course; }

    public int getRatingValue() { return ratingValue; }
    public void setRatingValue(int ratingValue) { this.ratingValue = ratingValue; }
} 