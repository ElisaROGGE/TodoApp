package src;

public class Task {
    private String title;
    private boolean completed;

    // Constructeur sans priorité
    public Task(String title) {
        this.title = title;
        this.completed = false;
    }

    // Constructeur avec priorité (surcharge)
    public Task(String title, String priority) {
        this.title = title;
        this.completed = false;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return completed ? "✔  " + title : "○  " + title;
    }
}