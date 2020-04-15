package dao.user;

import pojo.User;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    User getLoginUser(Connection connection,String UserCode) throws SQLException;
    int updatePwd(Connection connection,int id,String password)throws SQLException;
    int getUserCount(Connection connection, String username, int userRole) throws SQLException;
    List<User> getUserList(Connection connection,String userName,int userRole,int currentPageNo,int pageSize)throws SQLException;
    int add(Connection connection, User user)throws SQLException;
    int deleteUserById(Connection connection, Integer delId)throws SQLException;
    User getUserById(Connection connection, String id)throws SQLException;
    int modify(Connection connection, User user)throws SQLException;

}
