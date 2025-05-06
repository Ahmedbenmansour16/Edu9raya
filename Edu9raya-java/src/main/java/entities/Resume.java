package entities;

public class Resume {
    private int id;
    private String filename;
    private int stageId;
    private byte[] fileContent;

    public Resume(int id, String filename, int stageId) {
        this.id = id;
        this.filename = filename;
        this.stageId = stageId;
    }

    public Resume(String filename, int stageId) {
        this.filename = filename;
        this.stageId = stageId;
    }

    public Resume() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getStageId() {
        return stageId;
    }

    public void setStageId(int stageId) {
        this.stageId = stageId;
    }


    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    @Override
    public String toString() {
        return "Resume{" +
                "id=" + id +
                ", filename='" + filename + '\'' +
                ", stageId=" + stageId +
                '}';
    }

}
