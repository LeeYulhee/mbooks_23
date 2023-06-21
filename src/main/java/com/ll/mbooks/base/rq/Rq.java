package com.ll.mbooks.base.rq;

import com.ll.mbooks.base.dto.RsData;
import com.ll.mbooks.base.security.dto.MemberContext;
import com.ll.mbooks.domain.member.entity.Member;
import com.ll.mbooks.util.Ut;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Date;

@Component
@Slf4j
@RequestScope
public class Rq {
    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final MemberContext memberContext;
    @Getter
    private final Member member;

    public Rq(HttpServletRequest req, HttpServletResponse resp) {
        this.req = req;
        this.resp = resp;

        // 현재 로그인한 회원의 인증정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof MemberContext) {
            this.memberContext = (MemberContext) authentication.getPrincipal();
            this.member = memberContext.getMember();
        } else {
            this.memberContext = null;
            this.member = null;
        }
    }

    public String redirectToBackWithMsg(String msg) {
        String url = req.getHeader("Referer");

        if (StringUtils.hasText(url) == false) {
            url = "/";
        }

        return redirectWithMsg(url, msg);
    }

    public boolean hasAuthority(String authorityName) {
        if (memberContext == null) return false;

        return memberContext.hasAuthority(authorityName);
    }

    // 뒤로가기 + 메세지
    public String historyBack(String msg) {
        String referer = req.getHeader("referer");
        String key = "historyBackErrorMsg___" + referer;
        req.setAttribute("localStorageKeyAboutHistoryBackErrorMsg", key);
        req.setAttribute("historyBackErrorMsg", msg);
        // 200 이 아니라 400 으로 응답코드가 지정되도록
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return "common/js";
    }

    public String historyBack(RsData rsData) {
        return historyBack(rsData.getMsg());
    }

    public static String urlWithMsg(String url, RsData rsData) {
        if (rsData.isFail()) {
            return urlWithErrorMsg(url, rsData.getMsg());
        }

        return urlWithMsg(url, rsData.getMsg());
    }

    public static String urlWithMsg(String url, String msg) {
        return Ut.url.modifyQueryParam(url, "msg", msgWithTtl(msg));
    }

    public static String urlWithErrorMsg(String url, String errorMsg) {
        return Ut.url.modifyQueryParam(url, "errorMsg", msgWithTtl(errorMsg));
    }

    public String modifyQueryParam(String paramName, String paramValue) {
        return Ut.url.modifyQueryParam(getCurrentUrl(), paramName, paramValue);
    }

    private String getCurrentUrl() {
        String url = req.getRequestURI();
        String queryStr = req.getQueryString();

        if (StringUtils.hasText(queryStr)) {
            url += "?" + queryStr;
        }

        return url;
    }

    public static String redirectWithMsg(String url, RsData rsData) {
        return "redirect:" + urlWithMsg(url, rsData);
    }

    public static String redirectWithMsg(String url, String msg) {
        return "redirect:" + urlWithMsg(url, msg);
    }

    private static String msgWithTtl(String msg) {
        return Ut.url.encode(msg) + ";ttl=" + new Date().getTime();
    }

    public static String redirectWithErrorMsg(String url, RsData rsData) {
        url = Ut.url.modifyQueryParam(url, "errorMsg", msgWithTtl(rsData.getMsg()));

        return "redirect:" + url;
    }

    public long getId() {
        if (isLogout()) {
            return 0;
        }
        return getMember().getId();
    }

    public boolean isLogout() {
        return member == null;
    }

    public boolean isLogined() {
        return isLogout() == false;
    }

    public boolean isAdmin() {
        if (isLogout()) return false;

        return memberContext.hasAuthority("ADMIN");
    }

    public boolean isAuthor() {
        if (isLogout()) return false;

        return memberContext.hasAuthority("AUTHOR");
    }

    public boolean isUsrPage() {
        return isAdmPage() == false;
    }

    public boolean isAdmPage() {
        return req.getRequestURI().startsWith("/adm");
    }

    public HttpSession getSession() {
        return req.getSession();
    }

    public String getAvatar(Integer size) {
        String avatarFileName = member.getAvatarFileName();

        System.out.println("avatarFileName = " + avatarFileName);

        if ( StringUtils.hasLength(avatarFileName) ) {
            return """
                    <img width="%d" height="%d" src="%s">
                    """
                    .stripIndent()
                    .formatted(size, size, "/gen/member/" + avatarFileName);
        }

        return """
                <svg width="%d" height="%d" data-jdenticon-value="%s"></svg>
                """
                .stripIndent()
                .formatted(size, size, member.getJdenticon());
    }
}