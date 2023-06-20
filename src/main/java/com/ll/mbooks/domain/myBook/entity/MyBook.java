package com.ll.mbooks.domain.myBook.entity;


import com.ll.mbooks.base.entity.BaseEntity;
import com.ll.mbooks.domain.member.entity.Member;
import com.ll.mbooks.domain.order.entity.OrderItem;
import com.ll.mbooks.domain.product.entity.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.FetchType.LAZY;


@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class MyBook extends BaseEntity {
    @ManyToOne(fetch = LAZY)
    private Member owner;

    @ManyToOne(fetch = LAZY)
    @ToString.Exclude
    private Product product;

    @ManyToOne(fetch = LAZY)
    @ToString.Exclude
    private OrderItem orderItem;
}
