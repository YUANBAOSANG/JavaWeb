package servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import filter.SysFilter;
import pojo.Role;
import pojo.User;
import service.role.RoleService;
import service.role.RoleServiceImpl;
import service.user.UserService;
import service.user.UserServiceImpl;
import util.Constants;
import util.PageSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }
    enum EnumMethod{
        savepwd,pwdmodify,add,
        ucexist,query,getrolelist,
       deluser,view,modify,
       modifyexe;

    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EnumMethod method = EnumMethod.valueOf(req.getParameter("method"));
        System.out.println("-------进入了UserServlet"+method);
//        if("savepwd".equals(method)){
//            System.out.println("-------进入了更新密码子方法");
//            updatepwd(req,resp);
//        }
//        else if("pwdmodify".equals(method)){
//            System.out.println("-------进入了判断旧密码子方法");
//            pwdModify(req,resp);
        switch (method){
            case add:
                add(req, resp);
                break;
            case view:
                getUserById(req, resp,"userview.jsp");
                break;
            case query:
                query(req, resp);
                break;
            case modify:
                getUserById(req, resp,"usermodify.jsp");
                break;
            case deluser:
                delUser(req, resp);
                break;
            case savepwd:
                updatepwd(req, resp);
                break;
            case ucexist:
                userCodeExist(req, resp);
                break;
            case modifyexe:
                modify(req, resp);
                break;
            case pwdmodify:
                pwdModify(req, resp);
                break;
            case getrolelist:
                getRoleList(req, resp);
                break;
        }
    }

    private void getRoleList(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Role> roleList = null;
        RoleService roleService = new RoleServiceImpl();
        roleList = roleService.getRoleList();
        //把roleList转换成json对象输出
        resp.setContentType("application/json");
        PrintWriter outPrintWriter = resp.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(roleList));
        outPrintWriter.flush();
        outPrintWriter.close();
    }

    private void modify(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String id = req.getParameter("uid");
        String userName = req.getParameter("userName");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        User user = new User();
        user.setId(Integer.valueOf(id));
        user.setUserName(userName);
        user.setGender(Integer.valueOf(gender));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.valueOf(userRole));
        user.setModifyBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());

        UserService userService = new UserServiceImpl();
        if(userService.modify(user)){
            Constants.UPDATE_Count++;
            resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
        }else{
            req.getRequestDispatcher("usermodify.jsp").forward(req, resp);
        }
    }

    private void userCodeExist(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //判断用户账号是否可用
        String userCode = req.getParameter("userCode");

        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(StringUtils.isNullOrEmpty(userCode)){
            //userCode == null || userCode.equals("")
            resultMap.put("userCode", "exist");
        }else{
            UserService userService = new UserServiceImpl();
            User user = userService.selectUserCodeExist(userCode);
            if(null != user){
                resultMap.put("userCode","exist");
            }else{
                resultMap.put("userCode", "notexist");
            }
        }

        //把resultMap转为json字符串以json的形式输出
        //配置上下文的输出类型
        resp.setContentType("application/json");
        //从response对象中获取往外输出的writer对象
        PrintWriter outPrintWriter = resp.getWriter();
        //把resultMap转为json字符串 输出
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();//刷新
        outPrintWriter.close();//关闭流
    }

    private void delUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("uid");
        Integer delId = 0;
        try{
            delId = Integer.parseInt(id);
        }catch (Exception e) {
            // TODO: handle exception
            delId = 0;
        }
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(delId <= 0){
            resultMap.put("delResult", "notexist");
        }else{
            UserService userService = new UserServiceImpl();
            if(userService.deleteUserById(delId)){
                Constants.UPDATE_Count++;
                resultMap.put("delResult", "true");
            }else{
                resultMap.put("delResult", "false");
            }
        }

        //把resultMap转换成json对象输出
        resp.setContentType("application/json");
        PrintWriter outPrintWriter = resp.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();
        outPrintWriter.close();
    }

    private void query(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
            //查询用户列表
            String queryUserName = req.getParameter("queryname");
            String temp = req.getParameter("queryUserRole");
            String pageIndex = req.getParameter("pageIndex");
            int queryUserRole = 0;
            UserService userService = new UserServiceImpl();
            List<User> userList = null;
            //设置页面容量
            int pageSize = Constants.pageSize;
            //当前页码
            int currentPageNo = 1;
            /**
             * http://localhost:8090/SMBMS/userlist.do
             * ----queryUserName --NULL
             * http://localhost:8090/SMBMS/userlist.do?queryname=
             * --queryUserName ---""
             */
            System.out.println("queryUserName servlet--------"+queryUserName);
            System.out.println("queryUserRole servlet--------"+queryUserRole);
            System.out.println("query pageIndex--------- > " + pageIndex);
            if(queryUserName == null){
                queryUserName = "";
            }
            if(temp != null && !temp.equals("")){
                queryUserRole = Integer.parseInt(temp);
            }

            if(pageIndex != null){
                try{
                    currentPageNo = Integer.valueOf(pageIndex);
                }catch(NumberFormatException e){
                    resp.sendRedirect("error.jsp");
                }
            }
            //总数量（表）
            int totalCount	= userService.getUserCount(queryUserName,queryUserRole);
            //总页数
            PageSupport pages=new PageSupport();
            pages.setCurrentPageNo(currentPageNo);
            pages.setPageSize(pageSize);
            pages.setTotalCount(totalCount);

            int totalPageCount = pages.getTotalPageCount();

            //控制首页和尾页
            if(currentPageNo < 1){
                currentPageNo = 1;
            }else if(currentPageNo > totalPageCount){
                currentPageNo = totalPageCount;
            }


            userList = userService.getUserList(queryUserName,queryUserRole,currentPageNo, pageSize);
            req.setAttribute("userList", userList);
            List<Role> roleList = null;
            RoleService roleService = new RoleServiceImpl();
            roleList = roleService.getRoleList();
            req.setAttribute("roleList", roleList);
            req.setAttribute("queryUserName", queryUserName);
            req.setAttribute("queryUserRole", queryUserRole);
            req.setAttribute("totalPageCount", totalPageCount);
            req.setAttribute("totalCount", totalCount);
            req.setAttribute("currentPageNo", currentPageNo);
            req.getRequestDispatcher("userlist.jsp").forward(req, resp);
    }

    private void getUserById(HttpServletRequest req, HttpServletResponse resp, String url) throws ServletException, IOException {
        String id = req.getParameter("uid");
        if(!StringUtils.isNullOrEmpty(id)){
            //调用后台方法得到user对象
            UserService userService = new UserServiceImpl();
            User user = userService.getUserById(id);
            req.setAttribute("user", user);
            req.getRequestDispatcher(url).forward(req, resp);
        }
    }

    private void add(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        System.out.println("add()================");
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setAddress(address);
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        user.setGender(Integer.valueOf(gender));
        user.setPhone(phone);
        user.setUserRole(Integer.valueOf(userRole));
        user.setCreationDate(new Date());
        user.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());

        UserService userService = new UserServiceImpl();
        if(userService.add(user)){
            Constants.UPDATE_Count++;
            resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
        }else{
            req.getRequestDispatcher("useradd.jsp").forward(req, resp);
        }
    }

    public void updatepwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object attribute = req.getSession().getAttribute(Constants.USER_SESSION);
        String newpassword = req.getParameter("newpassword");
        boolean flag;
        if(attribute!=null && !StringUtils.isNullOrEmpty(newpassword)){
            UserService userService = new UserServiceImpl();
            flag = userService.updatePwd(((User)attribute).getId(),newpassword);
            if(flag){
                req.setAttribute("message","修改密码成功，请重新登录");
                req.getSession().removeAttribute(Constants.USER_SESSION);
            }else {
                req.setAttribute("message","修改密码失败");
            }
        }else {
            req.setAttribute("message","新密码有问题");
        }
        req.getRequestDispatcher("pwdmodify.jsp").forward(req,resp);
    }

    public void pwdModify(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Object attribute = req.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");
        Map<String,String> resultMap = new HashMap<>();
        System.out.println("-----进入Servlet修改密码的方法");
        if(attribute==null){
            System.out.println("------session过期");
            resultMap.put("result","sessionerror");
        }else if(StringUtils.isNullOrEmpty(oldpassword)){
            System.out.println("------旧密码为空");
            resultMap.put("result","error");
        }else {
            String userPassword = ((User)attribute).getUserPassword();
            if(oldpassword.equals(userPassword)){
                System.out.println("------旧密码匹配");
                resultMap.put("result","true");
            }else {
                System.out.println("------旧密码不匹配");
                resultMap.put("result","false");
            }
        }
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();

    }
}
