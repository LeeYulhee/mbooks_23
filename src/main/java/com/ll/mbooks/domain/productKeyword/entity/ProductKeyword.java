package com.ll.mbooks.domain.productKeyword.entity;

import com.ll.mbooks.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class ProductKeyword extends BaseEntity {

    private String content;

    public Object getListUrl() {
        return "/product/tag/" + content;
    }

    public long getExtra_postTagsCount() {
        return (long) getExtra().get("postTagsCount");
    }
}
