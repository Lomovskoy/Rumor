package ru.social.network.plugins;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RedirectInterceptor extends HandlerInterceptorAdapter {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            var args = request.getQueryString() != null ? request.getQueryString() : "";
            var url = request.getRequestURI() + "?" + args;
            response.setHeader("Turbolinks-Location", url);
        }
    }
}