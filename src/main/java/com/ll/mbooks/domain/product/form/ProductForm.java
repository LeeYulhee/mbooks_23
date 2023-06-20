package com.ll.mbooks.domain.product.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class ProductForm {
    @NotBlank
    private String subject;
    @NotNull
    private int price;
    @NotNull
    private Long postKeywordId;
    @NotBlank
    private String productTagContents;
}
