package com.mysite.sbb.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
	    HttpSession session = request.getSession(false);

	    if (session == null) {
	        throw new com.mysite.sbb.error.ApiException(HttpStatus.UNAUTHORIZED, "AUTH_401", "Not logged in");
	    }

	    Object hospitalId = session.getAttribute("HOSPITAL_ID");
	    Object adminId = session.getAttribute("ADMIN_ID");

	    // 병원도 아니고 관리자도 아니면 로그인 아님
	    if (hospitalId == null && adminId == null) {
	        throw new com.mysite.sbb.error.ApiException(HttpStatus.UNAUTHORIZED, "AUTH_401", "Not logged in");
	    }

	    return true;
	}
}