package edu.progmatic.messageapp.modell;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

public class Message {

    private static Long maxId = 0L;

    private Long id;

    @Size(max = 500, min = 3 , message = "Not between {2} and {1} characters!")
    @NotNull
    //@Pattern(regexp ="^[A-Z]\\w* [A-Z]\\w*", message = "Not a valid name!")
    private String author;

    @NotNull
    @NotBlank
    private String text;

    @DateTimeFormat(pattern = "yyyy/MMMM/dd HH:mm")
    private LocalDateTime creationDate;

    public Message() {
    }

    public Message(String author, String text, LocalDateTime creationDate) {
        this.id = maxId++;
        this.author = author;
        this.text = text;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
