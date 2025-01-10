package com.n3c3.rentroom.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
public class CommentPostDetailDTO {
    private Long id;
    private String comment;
    private String fullNameUserComment;
    private LocalDate commentDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFullNameUserComment() {
        return fullNameUserComment;
    }

    public void setFullNameUserComment(String fullNameUserComment) {
        this.fullNameUserComment = fullNameUserComment;
    }

    public LocalDate getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(LocalDate commentDate) {
        this.commentDate = commentDate;
    }
}
