package com.ll.mbooks.domain.emailVerification.service;


import com.ll.mbooks.base.dto.RsData;
import com.ll.mbooks.domain.member.entity.Member;
import com.ll.mbooks.domain.member.service.MemberService;
import com.ll.mbooks.util.Ut;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class EmailVerificationServiceTests {
    @Autowired
    private EmailVerificationService emailVerificationService;
    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("회원의 이메일 인증코드 생성")
    void t1() {
        String code = emailVerificationService.genEmailVerificationCode(1);

        assertThat(code).hasSizeGreaterThan(20);
    }

    @Test
    @DisplayName("이메일 인증코드 유효성검사")
    void t2() {
        String code = emailVerificationService.genEmailVerificationCode(1);
        RsData rsData = emailVerificationService.verifyVerificationCode(1, code);

        assertThat(rsData.isSuccess()).isTrue();
    }

    @Test
    @DisplayName("이메일 인증코드 URL 생성")
    void t3() {
        String url = emailVerificationService.genEmailVerificationUrl(1);

        Assertions.assertThat(Ut.url.isUrl(url)).isTrue();
    }

    @Test
    @DisplayName("회원가입이 완료되면 유효한 이메일인증코드가 발급된다")
    void t4() {
        Member member = memberService.join("user3", "1234", "user3@test.com", null).getData().getMember();
        String verificationCode = emailVerificationService.findEmailVerificationCode(member.getId());

        boolean isSuccess = emailVerificationService.verifyVerificationCode(member.getId(), verificationCode).isSuccess();

        assertThat(isSuccess).isTrue();
    }

    @Test
    @DisplayName("회원가입 후 이메일인증코드로 인증을 완료하면 해당 회원은 이메일인증된 회원이 된다")
    public void t5() {
        MemberService.JoinResponse joinResponse = memberService.join("user3", "1234", "user3@test.com", null).getData();
        Member member = joinResponse.getMember();
        joinResponse.getSendRsFuture().join();
        String verificationCode = emailVerificationService.findEmailVerificationCode(member.getId());

        boolean isSuccess = memberService.verifyEmail(member.getId(), verificationCode).isSuccess();
        assertThat(isSuccess).isTrue();

        assertThat(member.isEmailVerified()).isTrue();
    }
}