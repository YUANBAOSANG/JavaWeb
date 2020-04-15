package dao.email;
import dao.BaseDao;
import org.junit.Test;
import pojo.Email;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EmailDaoImpl implements EmailDao{
    @Override
    public List<String> getEmailList(Connection connection) throws SQLException {
        String sql = "SELECT email FROM smbms_email ";
        List<String> list = new ArrayList<>();
        if(Objects.nonNull(connection)){
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = BaseDao.executeSelect(connection,preparedStatement,sql,null);
            while (resultSet.next()){
                list.add(resultSet.getString("email"));
            }
            BaseDao.closeSQLResource(null,preparedStatement,resultSet);
        }
        return list;
    }

    @Override
    public Email getEmailById(Connection connection, String id) throws SQLException {
        String sql = "SELECT * FROM smbms_email where userid = ?";
        Email email = null;
        if(Objects.nonNull(connection)){
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = BaseDao.executeSelect(connection,preparedStatement,sql,new Object[]{id});
            if (resultSet.next()){
                email = new Email();
                email.setEmail(resultSet.getString("email"));
                email.setCode(resultSet.getString("code"));
                email.setId(resultSet.getInt("userid"));
            }
            BaseDao.closeSQLResource(null,preparedStatement,resultSet);
        }
        return email;
    }

    @Override
    public int updateCode(Connection connection, String id,String code) throws Exception {
        if(id.equals("-1")){
            throw new Exception("id不能为-1！！！");
        }
        String sql = "UPDATE smbms_email SET code=? where userid = ?";
        int row = 0;
        if(Objects.nonNull(connection)){
            PreparedStatement preparedStatement = null;
            row = BaseDao.executeOther(connection,preparedStatement,sql,new Object[]{code,id});
        }
        return row;
    }
}
