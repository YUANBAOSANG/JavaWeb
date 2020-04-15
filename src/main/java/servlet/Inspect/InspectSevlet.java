package servlet.Inspect;

import pojo.User;
import service.email.EmailServeImol;
import service.email.EmailService;
import util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class InspectSevlet extends HttpServlet {
    static int count;
    static boolean flag = true;
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        if(!Constants.Code.equals(code)){
            if(flag&&(++count)>=3){
                flag = false;
                User user = (User) req.getSession().getAttribute(Constants.USER_SESSION);
                EmailService emailService = new EmailServeImol();
                String msg = "id："+user.getId()+"<br/>姓名："+user.getUserName()+"<br/>电话："+user.getPhone();
                emailService.massEmail(msg,"【警告】该员工存在被盗号嫌疑！");
            }
            req.setAttribute("message","验证码输入错误，请重新尝试");

            req.getRequestDispatcher("inspect.jsp").forward(req,resp);
        }else {
            resp.sendRedirect("frame.jsp");
        }


    }
}
