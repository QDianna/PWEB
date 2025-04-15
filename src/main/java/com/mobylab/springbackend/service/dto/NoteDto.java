package com.mobylab.springbackend.service.dto;

import java.util.UUID;

public class NoteDto {

    private UUID id;
    private String title;
    private String content;

    public UUID getId() {
        return id;
    }

    public NoteDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public NoteDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public NoteDto setContent(String content) {
        this.content = content;
        return this;
    }

}
