package dao.role;

import dao.BaseDao;
import pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao {
    @Override
    public List<Role> getRoleList(Connection connection) throws Exception {
        String sql = "SELECT * FROM smbms_role";
        PreparedStatement preparedStatement = null;
        List<Role> roleList = new ArrayList<>();
        ResultSet resultSet = BaseDao.executeSelect(connection,preparedStatement,sql,null);
        while(resultSet.next()){
            Role _role = new Role();
            _role.setId(resultSet.getInt("id"));
            _role.setRoleCode(resultSet.getString("roleCode"));
            _role.setRoleName(resultSet.getString("roleName"));
            roleList.add(_role);
        }
        return roleList;
    }
}
