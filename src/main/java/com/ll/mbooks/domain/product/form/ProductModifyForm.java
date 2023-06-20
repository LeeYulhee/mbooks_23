package com.ll.mbooks.domain.product.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductModifyForm {
    @NotBlank
    private String subject;
    @NotNull
    private int price;
    @NotBlank
    private String productTagContents;
}
