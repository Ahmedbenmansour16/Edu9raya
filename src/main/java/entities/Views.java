package entities;

public class Views {

    private int id;
    private Module module;
    private int viewCount;

    public Views() {
    }

    public Views(Module module, int viewCount) {
        this.module = module;
        this.viewCount = viewCount;
    }

    public Views(int id, Module module, int viewCount) {
        this.id = id;
        this.module = module;
        this.viewCount = viewCount;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    @Override
    public String toString() {
        return "Views{" +
                "id=" + id +
                ", module=" + (module != null ? module.getId() : "null") +
                ", viewCount=" + viewCount +
                '}';
    }
} 