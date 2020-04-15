package dao.user;

import com.mysql.cj.util.StringUtils;
import dao.BaseDao;
import pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao{
    @Override
    public User getLoginUser(Connection connection,String  UserCode) throws SQLException {
        String sql = "SELECT * FROM smbms_user where usercode='"+UserCode+"'";
        ResultSet rs;
        User user = null;
        PreparedStatement preparedStatement = null;
        if(connection!=null) {
            rs = BaseDao.executeSelect(connection, preparedStatement, sql, null);
            while (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getTimestamp("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));
            }

            BaseDao.closeSQLResource(null, preparedStatement, rs);
        }
        return user;
    }

    @Override
    public int updatePwd(Connection connection, int id, String password) throws SQLException {
        String sql = "UPDATE smbms_user SET userpassword = ? where id= ?";
        PreparedStatement preparedStatement = null;
        int executeRow = -1;
        if (connection != null) {
            executeRow =  BaseDao.executeOther(connection, preparedStatement, sql, new Object[]{password, id});
            BaseDao.closeSQLResource(null, preparedStatement, null);

        }
        return executeRow;
    }

    @Override
    public int getUserCount(Connection connection, String username, int userRole) throws SQLException {

        PreparedStatement preparedStatement = null;
        ResultSet rs;
        int count = 0;
        if(connection!=null){
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT COUNT(1) AS count FROM smbms_role r INNER join smbms_user u ON u.userRole = r.id");
            boolean flag =true;
            List<Object> list = new ArrayList<>();
            if(!StringUtils.isNullOrEmpty(username)){
                if(flag){
                    sql.append(" where");
                    flag = false;
                }
                sql.append(" u.userName like ?");
                list.add("%"+username+"%");
            }
            if(userRole>0){
                if(flag){
                    sql.append(" where");
                    flag = false;
                }else {
                    sql.append(" and");
                }
                sql.append(" u.userRole = ?");
                list.add(userRole);
            }
            Object[] objects = list.toArray();
            System.out.println("UserDaoImpl->getUserCount:"+sql.toString());
            rs = BaseDao.executeSelect(connection,preparedStatement, String.valueOf(sql),objects);
            if(rs.next()){
              count =  rs.getInt("count");
            }
            BaseDao.closeSQLResource(null,preparedStatement,rs);
        }
        return count;
    }

    @Override
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs;
        List<User> userList = new ArrayList<User>();
        if(connection != null){
            StringBuffer sql = new StringBuffer();
            sql.append("select u.*,r.roleName as userRoleName from smbms_role r INNER JOIN smbms_user u ON u.userRole = r.id");
            List<Object> list = new ArrayList<Object>();
            if(!StringUtils.isNullOrEmpty(userName)){
                sql.append(" AND u.userName LIKE ?");
                list.add("%"+userName+"%");
            }
            if(userRole > 0){
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }
            sql.append(" order by creationDate DESC limit ?,?");
            currentPageNo = (currentPageNo-1)*pageSize;
            list.add(currentPageNo);
            list.add(pageSize);

            Object[] params = list.toArray();
            System.out.println("sql ----> " + sql.toString());
            rs = BaseDao.executeSelect(connection, pstm,  sql.toString(), params);
            while(rs.next()){
                User _user = new User();
                _user.setId(rs.getInt("id"));
                _user.setUserCode(rs.getString("userCode"));
                _user.setUserName(rs.getString("userName"));
                _user.setGender(rs.getInt("gender"));
                _user.setBirthday(rs.getDate("birthday"));
                _user.setPhone(rs.getString("phone"));
                _user.setUserRole(rs.getInt("userRole"));
                _user.setUserRoleName(rs.getString("userRoleName"));
                userList.add(_user);
            }
            BaseDao.closeSQLResource(null, pstm, rs);
        }
        return userList;
    }

    @Override
    public int add(Connection connection, User user) throws SQLException {
        PreparedStatement pstm = null;
        int updateRows = 0;
        if(null != connection){
            String sql = "insert into smbms_user (userCode,userName,userPassword," +
                    "userRole,gender,birthday,phone,address,creationDate,createdBy) " +
                    "values(?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {user.getUserCode(),user.getUserName(),user.getUserPassword(),
                    user.getUserRole(),user.getGender(),user.getBirthday(),
                    user.getPhone(),user.getAddress(),user.getCreationDate(),user.getCreatedBy()};
            updateRows = BaseDao.executeOther(connection, pstm, sql, params);
            BaseDao.closeSQLResource(null, pstm, null);
        }
        return updateRows;
    }

    @Override
    public int deleteUserById(Connection connection, Integer delId) throws SQLException {
        PreparedStatement pstm = null;
        int flag = 0;
        if(null != connection){
            String sql = "delete from smbms_user where id=?";
            Object[] params = {delId};
            flag = BaseDao.executeOther(connection, pstm, sql, params);
            BaseDao.closeSQLResource(null, pstm, null);
        }
        return flag;
    }

    @Override
    public User getUserById(Connection connection, String id) throws SQLException {
        User user = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        if(null != connection){
            String sql = "select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.id=? and u.userRole = r.id";
            Object[] params = {id};
            rs = BaseDao.executeSelect(connection, pstm, sql, params);
            if(rs.next()){
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getTimestamp("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));
                user.setUserRoleName(rs.getString("userRoleName"));
            }
            BaseDao.closeSQLResource(null, pstm, rs);
        }
        return user;
    }

    @Override
    public int modify(Connection connection, User user) throws SQLException {
        int flag = 0;
        PreparedStatement pstm = null;
        if(null != connection){
            String sql = "update smbms_user set userName=?,"+
                    "gender=?,birthday=?,phone=?,address=?,userRole=?,modifyBy=?,modifyDate=? where id = ? ";
            Object[] params = {user.getUserName(),user.getGender(),user.getBirthday(),
                    user.getPhone(),user.getAddress(),user.getUserRole(),user.getModifyBy(),
                    user.getModifyDate(),user.getId()};
            flag = BaseDao.executeOther(connection, pstm, sql, params);
            BaseDao.closeSQLResource(null, pstm, null);
        }
        return flag;
    }
}
