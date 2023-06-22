package com.ll.mbooks.domain.member.controller;

import com.ll.mbooks.base.dto.RsData;
import com.ll.mbooks.base.rq.Rq;
import com.ll.mbooks.domain.member.entity.Member;
import com.ll.mbooks.domain.member.exception.AlreadyJoinException;
import com.ll.mbooks.domain.member.form.JoinForm;
import com.ll.mbooks.domain.member.service.MemberService;
import com.ll.mbooks.util.Ut;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;


@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final Rq rq;
    @Value("${custom.genFileDirPath}")
    private String genFileDirPath;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLogin(HttpServletRequest request) {
        String uri = request.getHeader("Referer");
        if (uri != null && !uri.contains("/member/login")) {
            request.getSession().setAttribute("prevPage", uri);
        }

        return "member/login";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin() {
        return "member/join";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm) {
        try {
            memberService.join(joinForm.getUsername(), joinForm.getPassword(), joinForm.getEmail(), joinForm.getNickname());
        } catch (AlreadyJoinException e) {
            return rq.historyBack(e.getMessage());
        }

        return Rq.redirectWithMsg("/member/login", "회원가입이 완료되었습니다.");
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/findUsername")
    public String showFindUsername() {
        return "member/findUsername";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/findUsername")
    public String findUsername(String email, Model model) {
        Member member = memberService.findByEmail(email).orElse(null);

        if (member == null) {
            return rq.historyBack("일치하는 회원이 존재하지 않습니다.");
        }

        return Rq.redirectWithMsg("/member/login?username=%s".formatted(member.getUsername()), "해당 이메일로 가입한 계정의 아이디는 '%s' 입니다.".formatted(member.getUsername()));
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/findPassword")
    public String showFindPassword() {
        return "member/findPassword";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/findPassword")
    public String findPassword(String username, String email, Model model) {
        Member member = memberService.findByUsernameAndEmail(username, email).orElse(null);

        if (member == null) {
            return rq.historyBack("일치하는 회원이 존재하지 않습니다.");
        }

        RsData sendTempLoginPwToEmailResultData = memberService.sendTempPasswordToEmail(member);

        if (sendTempLoginPwToEmailResultData.isFail()) {
            return rq.historyBack(sendTempLoginPwToEmailResultData);
        }

        return Rq.redirectWithMsg("/member/login?username=%s".formatted(member.getUsername()), "해당 이메일로 '%s' 계정의 임시비번을 발송했습니다.".formatted(member.getUsername()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String profile(Model model) {
        long actorRestCash = memberService.getRestCash(rq.getMember());
        model.addAttribute("actorRestCash", actorRestCash);
        return "member/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modifyPassword")
    public String showModifyPassword() {
        return "member/modifyPassword";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modifyPassword")
    public String modifyPassword(String oldPassword, String password) {
        Member member = rq.getMember();
        RsData rsData = memberService.modifyPassword(member, password, oldPassword);

        if (rsData.isFail()) {
            return rq.historyBack(rsData.getMsg());
        }

        return Rq.redirectWithMsg("/", rsData);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/beAuthor")
    public String showBeAuthor() {
        return "member/beAuthor";
    }

    @SneakyThrows
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/beAuthor")
    public String beAuthor(MultipartFile avatar, String nickname) {
        Member member = rq.getMember();

        String avatarFileName = null;

        if (avatar != null && !avatar.isEmpty()) {
            avatarFileName = "%d".formatted(member.getId()) + "." + Ut.file.getExt(avatar.getOriginalFilename());
            File destFile = new File(genFileDirPath + "/member/" + avatarFileName);
            destFile.mkdirs();
            avatar.transferTo(destFile);
        }

        RsData rsData = memberService.beAuthor(member.getUsername(), nickname, avatarFileName);

        if (rsData.isFail()) {
            return Rq.redirectWithMsg("/member/beAuthor", rsData);
        }

        // 레디스에 저장되어 있는 memberMap 을 날림
        memberService.evictMemberMapByUsername__cached(member.getUsername());

        return Rq.redirectWithMsg("/", rsData);
    }
}