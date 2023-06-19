package com.ll.mbooks.app.security.handler;

import com.ll.mbooks.app.base.rq.Rq;
import com.ll.mbooks.app.member.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final MemberService memberService;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();

        if (memberService.shouldChangePasswordDueToAge(username)) {
            redirectStrategy.sendRedirect(request, response, Rq.urlWithErrorMsg("/member/modifyPassword", "비밀번호가 오래되었습니다. 변경해주세요."));
            clearAuthenticationAttributes(request);
            return;
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}