package com.ll.mbooks.domain.myBook.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MyBooksResponse {
    private List<MyBookDto> myBooks;
}
