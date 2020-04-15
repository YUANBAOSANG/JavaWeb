package dao.provider;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import pojo.Provider;

public interface ProviderDao {
    int add(Connection connection, Provider provider)throws SQLException;
    List<Provider> getProviderList(Connection connection, String proName, String proCode)throws SQLException;
    int deleteProviderById(Connection connection, String delId)throws SQLException;


    Provider getProviderById(Connection connection, String id)throws SQLException;

    int modify(Connection connection, Provider provider)throws SQLException;
}
