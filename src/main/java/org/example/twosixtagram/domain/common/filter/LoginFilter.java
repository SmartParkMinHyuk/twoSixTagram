package org.example.twosixtagram.domain.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.twosixtagram.domain.user.dto.UserResponse;
import org.example.twosixtagram.domain.user.entity.User;
import org.example.twosixtagram.domain.user.entity.UserStatus;
import org.example.twosixtagram.domain.user.repository.UserRepository;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
public class LoginFilter implements Filter {

    private final UserRepository userRepository;

    private static final String[] WHITE_LIST = {
            "/api/unauthenticated", // 로그인 하지않은 사용자
            "/api/users/login", // 로그인 요청
            // 회원가입 POST만 doFilter내 존재
    };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;

        String uri = httpReq.getRequestURI();
        String method = httpReq.getMethod();

        // 회원가입 POST만 화이트리스트에 포함, 공용경로 -> 회원탈퇴(DELETE) 화이트리스트에 미포함
        boolean isSignUp = uri.equals("/api/users") && method.equals("POST");

        if (isSignUp || isWhiteListed(uri)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = httpReq.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            httpRes.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpRes.setCharacterEncoding("UTF-8");
            httpRes.setContentType("application/json; charset=UTF-8");
            httpRes.getWriter().write("{\"message\": \"로그인이 필요합니다.\"}");
            return;
        }

        Long userId = (Long) session.getAttribute("userId");
        User user = userRepository.findById(userId).orElse(null);

        if (user == null || user.getStatus() != UserStatus.ACTIVE) {
            httpRes.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpRes.setCharacterEncoding("UTF-8");
            httpRes.setContentType("application/json; charset=UTF-8");
            httpRes.getWriter().write("{\"message\": \"탈퇴 또는 비활성화된 사용자입니다.\"}");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isWhiteListed(String uri) {

        return Arrays.asList(WHITE_LIST).contains(uri);

    }

}
