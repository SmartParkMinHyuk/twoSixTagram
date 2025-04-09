package org.example.twosixtagram.domain.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.twosixtagram.domain.user.dto.UserResponse;

import java.io.IOException;
import java.util.Arrays;


public class LoginFilter implements Filter {

    private static final String[] WHITE_LIST = {
            "/api/unauthenticated",
            "/api/users/login",
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

        Object userId = httpReq.getSession(false).getAttribute("userId");
        System.out.println("TEST FILTER USERID : " + userId); // 테스트 후 삭제 필요

        if (userId == null) {
            httpRes.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpRes.setContentType("application/json");
            httpRes.getWriter().write("{\"message\": \"로그인이 필요합니다.\"}");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isWhiteListed(String uri) {
//        return Arrays.asList(WHITE_LIST).contains(uri);
        return true; // 수정필요 모든 요청 허용
    }

}
