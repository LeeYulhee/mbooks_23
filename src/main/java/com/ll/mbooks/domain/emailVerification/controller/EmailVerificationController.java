package com.ll.mbooks.domain.emailVerification.controller;

import com.ll.mbooks.base.dto.RsData;
import com.ll.mbooks.base.rq.Rq;
import com.ll.mbooks.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/emailVerification")
public class EmailVerificationController {
    private final MemberService memberService;
    private final Rq rq;

    @GetMapping("/verify")
    public String verify(long memberId, String code) {
        RsData verifyEmailRsData = memberService.verifyEmail(memberId, code);

        if (verifyEmailRsData.isFail()) {
            return Rq.redirectWithMsg("/", verifyEmailRsData);
        }

        String successMsg = verifyEmailRsData.getMsg();

        if (rq.isLogout()) {
            return Rq.redirectWithMsg("/member/login", successMsg);
        }

        return Rq.redirectWithMsg("/", successMsg);
    }
}
