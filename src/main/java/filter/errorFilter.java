package filter;

import pojo.User;
import service.email.EmailServeImol;
import service.email.EmailService;
import util.Constants;

import javax.servlet.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class errorFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response1 = (HttpServletResponse) servletResponse;
        if(Objects.nonNull(request.getSession().getAttribute(Constants.USER_SESSION))){
            request.getSession().removeAttribute(Constants.USER_SESSION);
        }
        filterChain.doFilter(request,response1);
    }

    @Override
    public void destroy() {

    }
}
