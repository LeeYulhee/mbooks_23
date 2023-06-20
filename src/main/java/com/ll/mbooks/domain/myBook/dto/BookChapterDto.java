package com.ll.mbooks.domain.myBook.dto;

import com.ll.mbooks.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BookChapterDto {
    private long id;
    private String subject;
    private String content;
    private String contentHtml;

    public static BookChapterDto of(Post post) {
        return BookChapterDto
                .builder()
                .id(post.getId())
                .subject(post.getSubject())
                .content(post.getContent())
                .contentHtml(post.getContentHtml())
                .build();
    }
}
