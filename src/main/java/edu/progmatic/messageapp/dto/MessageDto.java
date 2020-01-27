package edu.progmatic.messageapp.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MessageDto {

    @NotBlank
    @NotNull
    private String text;

    @NotNull
    private Long topicId;


    public MessageDto() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }
}
