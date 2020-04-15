package service.role;

import dao.BaseDao;
import dao.role.RoleDao;
import dao.role.RoleDaoImpl;
import pojo.Role;

import java.sql.Connection;
import java.util.List;


public class RoleServiceImpl implements RoleService{
	
	private RoleDao roleDao;
	
	public RoleServiceImpl(){
		roleDao = new RoleDaoImpl();
	}
	
	@Override
	public List<Role> getRoleList() {
		Connection connection = null;
		List<Role> roleList = null;
		try {
			connection = BaseDao.getConnection();
			roleList = roleDao.getRoleList(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			BaseDao.closeSQLResource(connection, null, null);
		}
		return roleList;
	}
	
}
