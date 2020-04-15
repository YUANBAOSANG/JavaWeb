package filter;


import pojo.User;
import util.Constants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SysFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request1 = (HttpServletRequest) request;
        HttpServletResponse response1 = (HttpServletResponse) response;

        User user = (User) request1.getSession().getAttribute(Constants.USER_SESSION);
        if(user==null){
            response1.sendRedirect("/supermarket_war/error.jsp");
        } else{
            chain.doFilter(request,response);
        }
    }

    @Override
    public void destroy() {

    }
}
