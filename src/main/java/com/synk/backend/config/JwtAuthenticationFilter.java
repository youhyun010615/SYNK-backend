// 사용자가 로그인을 한 뒤에 게시글을 쓰거나 내 정보를 수정하려고 할 때, 매번 아이디/비밀번호를 보낼 수 는 없다.
// 대신 서버가 발행해준 JWT(전자 입장권)를 보여주는데, 이 코드는 그 입장권이 진짜인지 검사하고 "아, 이 이사람은 철수구나!" 라고 인정해주는 역할

//"클라이언트가 보낸 HTTP 헤더에서 JWT 토큰을 꺼내서, 진짜인지 확인한 다음, 서비스가 이 사용자를 믿고 작업을 수행할 수 있도록 인증 정보를 등록해주는 필터"입니다.

package com.synk.backend.config;

import com.synk.backend.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {  // OncePerRequestFilter : 사용자의 요청이 들어올 때 딱 한 번만 실행되도록 보장하는 필터

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);

        if (token != null && jwtUtil.validateToken(token)) {  // jwtUtil.validateToken(token) : 가짜인지 확인하기
            String userId = jwtUtil.extractUserId(token);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, null,
                            List.of());


            SecurityContextHolder.getContext().setAuthentication(authentication); // 토큰이 진짜라면, 그 안에 적힌 사용자 아이디를 꺼내서 '아, 이 요청은 인증된 사용자의 요청이다"라고 스프링 시큐리티 시스템에 등록
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {  // 입장권 꺼내기 : 순수한 토큰(문자열)만 추출
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
