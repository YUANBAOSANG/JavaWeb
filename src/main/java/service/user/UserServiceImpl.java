package service.user;
import java.sql.Connection;

import com.mysql.cj.util.StringUtils;
import dao.BaseDao;
import dao.user.UserDao;
import dao.user.UserDaoImpl;
import org.junit.Test;
import pojo.User;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserServiceImpl implements UserService {
    private UserDao userDao;
    public UserServiceImpl(){
        userDao = new UserDaoImpl();
    }
    @Override
    public User login(String userCode, String password) {
        Connection connection = BaseDao.getConnection();
        User user = null;
        try {
            user = userDao.getLoginUser(connection,userCode);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        BaseDao.closeSQLResource(connection,null,null);
        return check(password,user);
    }

    @Override
    public boolean updatePwd(int id, String password) {
        boolean flag = false;
        Connection connection = null;
        try {
             connection = BaseDao.getConnection();
             if(userDao.updatePwd(connection,id,password)>0){
                 flag = true;
             }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeSQLResource(connection,null,null);
        }
        return flag;
    }

    @Override
    public int getUserCount(String username,int cuserRole){
        Connection connection = null;
        int count =0;
        try {
            connection = BaseDao.getConnection();
            count = userDao.getUserCount(connection,username,cuserRole);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeSQLResource(connection,null,null);
        }
        return count;
    }

    @Override
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        Connection connection = null;
        List<User> userList = null;
        try {
            connection = BaseDao.getConnection();
            userList = userDao.getUserList(connection, queryUserName,queryUserRole,currentPageNo,pageSize);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            BaseDao.closeSQLResource(connection, null, null);
        }
        return userList;
    }

    @Override
    public boolean add(User user) {
        boolean flag = false;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            //开启JDBC事务管理
            connection.setAutoCommit(false);
            int updateRows = userDao.add(connection,user);
            connection.commit();
            if(updateRows > 0){
                flag = true;
                System.out.println("add success!");
            }else{
                System.out.println("add failed!");
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {
                System.out.println("rollback==================");
                connection.rollback();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }finally{
            //在service层进行connection连接的关闭
            BaseDao.closeSQLResource(connection, null, null);
        }
        return flag;
    }

    @Override
    public User selectUserCodeExist(String userCode) {
        Connection connection = null;
        User user = null;
        try {
            connection = BaseDao.getConnection();
            user = userDao.getLoginUser(connection, userCode);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            BaseDao.closeSQLResource(connection, null, null);
        }
        return user;
    }

    @Override
    public boolean deleteUserById(Integer delId) {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            if(userDao.deleteUserById(connection,delId) > 0) {
                flag = true;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            BaseDao.closeSQLResource(connection, null, null);
        }
        return flag;
    }

    @Override
    public User getUserById(String id) {
        User user = null;
        Connection connection = null;
        try{
            connection = BaseDao.getConnection();
            user = userDao.getUserById(connection,id);
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            user = null;
        }finally{
            BaseDao.closeSQLResource(connection, null, null);
        }
        return user;
    }

    @Override
    public boolean modify(User user) {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            if(userDao.modify(connection,user) > 0) {
                flag = true;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            BaseDao.closeSQLResource(connection, null, null);
        }
        return flag;
    }

    @Test
public void test(){
        UserService userService = new UserServiceImpl();
         List<User> list =userService.getUserList(null,-1,1,5);
        for (User u:list) {
            System.out.println("空参："+u.getUserName());
        }

        List<User> list1 =userService.getUserList("源大彪",-1,1,5);
        for (User u:list1) {
            System.out.println("username："+u.getUserName());
        }

        List<User> list3 =userService.getUserList("源大彪",1,1,5);
        for (User u:list3) {
            System.out.println("username+role："+u.getUserName());
        }
}


    private User check(String passwordLogin,User user){
        if(user!=null){
            return user.getUserPassword().equals(passwordLogin)?user:null;
        }
        return null;
    }


}
