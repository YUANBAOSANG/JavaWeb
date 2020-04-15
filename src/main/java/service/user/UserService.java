package service.user;

import pojo.User;
import java.sql.Connection;
import java.util.List;

public interface UserService {
    User login(String userCode,String password);
    boolean updatePwd(int id,String password);
    int getUserCount(String username,int cuserRole);
    List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize);
    boolean add(User user);
    User selectUserCodeExist(String userCode);
    boolean deleteUserById(Integer delId);
    User getUserById(String id);
    boolean modify(User user);

}
