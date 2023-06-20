package com.ll.mbooks.domain.post.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostForm {
    @NotBlank
    private String subject;
    @NotBlank
    private String content;
    @NotBlank
    private String contentHtml;
    @NotBlank
    private String postTagContents;
}
