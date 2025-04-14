package model;

public class Book {
    private int id;
    private String idBook;
    private String nomBook;
    private int catBook;
    private String dispoBook;
    private String description;
    private String pdfFile;
    private String fileId;
    private String picture;

    public Book() {}

    public Book(int id, String idBook, String nomBook, int catBook, String dispoBook,
                String description, String pdfFile, String fileId, String picture) {
        this.id = id;
        this.idBook = idBook;
        this.nomBook = nomBook;
        this.catBook = catBook;
        this.dispoBook = dispoBook;
        this.description = description;
        this.pdfFile = pdfFile;
        this.fileId = fileId;
        this.picture = picture;
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIdBook() { return idBook; }
    public void setIdBook(String idBook) { this.idBook = idBook; }

    public String getNomBook() { return nomBook; }
    public void setNomBook(String nomBook) { this.nomBook = nomBook; }

    public int getCatBook() { return catBook; }
    public void setCatBook(int catBook) { this.catBook = catBook; }

    public String getDispoBook() { return dispoBook; }
    public void setDispoBook(String dispoBook) { this.dispoBook = dispoBook; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPdfFile() { return pdfFile; }
    public void setPdfFile(String pdfFile) { this.pdfFile = pdfFile; }

    public String getFileId() { return fileId; }
    public void setFileId(String fileId) { this.fileId = fileId; }

    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", idBook='" + idBook + '\'' +
                ", nomBook='" + nomBook + '\'' +
                ", catBook=" + catBook +
                ", dispoBook='" + dispoBook + '\'' +
                ", description='" + description + '\'' +
                ", pdfFile='" + pdfFile + '\'' +
                ", fileId='" + fileId + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }
}
