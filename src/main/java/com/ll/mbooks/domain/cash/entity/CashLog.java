package com.ll.mbooks.domain.cash.entity;


import com.ll.mbooks.base.entity.BaseEntity;
import com.ll.mbooks.domain.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.FetchType.LAZY;


@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class CashLog extends BaseEntity {
    private String relTypeCode;
    private Long relId;
    @ManyToOne(fetch = LAZY)
    private Member member;
    private long price; // 변동
    @Enumerated(EnumType.STRING)
    private EvenType eventType;

    public CashLog(long id) {
        super(id);
    }

    public enum EvenType {
        충전__무통장입금,
        충전__토스페이먼츠,
        출금__통장입금,
        사용__토스페이먼츠_주문결제,
        사용__예치금_주문결제,
        환불__예치금_주문결제,
        작가정산__예치금;
    }
}
