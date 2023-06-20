package com.ll.mbooks.domain.myBook.dto;

import com.ll.mbooks.domain.myBook.entity.MyBook;
import com.ll.mbooks.domain.product.dto.ProductDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class MyBookDto {
    private long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private long ownerId;
    private ProductDto product;

    public static MyBookDto of(MyBook myBook) {
        return MyBookDto
                .builder()
                .id(myBook.getId())
                .createDate(myBook.getCreateDate())
                .modifyDate(myBook.getModifyDate())
                .ownerId(myBook.getOwner().getId())
                .product(ProductDto.of(myBook.getProduct()))
                .build();
    }
}
