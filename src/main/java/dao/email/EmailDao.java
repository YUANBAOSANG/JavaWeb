package dao.email;
import pojo.Email;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface EmailDao {
    List<String> getEmailList(Connection connection) throws SQLException;
    Email getEmailById(Connection connection, String id) throws SQLException;
    int updateCode(Connection connection,String id,String code) throws Exception;
}
