package servlet.user;


import pojo.User;
import service.email.EmailServeImol;
import service.email.EmailService;
import service.user.UserService;
import service.user.UserServiceImpl;
import util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userCode = req.getParameter("userCode");
        String passWord = req.getParameter("userPassword");

        UserService userService = new UserServiceImpl();
        User user = userService.login(userCode,passWord);
        if(user!=null){
            req.getSession().setAttribute(Constants.USER_SESSION,user);
            resp.sendRedirect(req.getContextPath()+"/jsp/inspect.jsp");
            EmailService email = new EmailServeImol();
            email.setCode(String.valueOf(user.getId()));
            email.sendEmail(String.valueOf(user.getId()),user.getUserName()+"你好!<br/>验证码为："+Constants.Code,"验证码");
        }
        else{
            req.setAttribute("error","密码或账号错误");
            req.getRequestDispatcher("login.jsp").forward(req,resp);
        }
    }
}
