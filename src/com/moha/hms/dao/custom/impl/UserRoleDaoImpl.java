package com.moha.hms.dao.custom.impl;

import com.moha.hms.dao.CrudUtil;
import com.moha.hms.dao.custom.UserRoleDao;
import com.moha.hms.entity.UserRole;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRoleDaoImpl implements UserRoleDao {
    @Override
    public boolean create(UserRole userRole) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO user_role VALUES (?,?,?)",
                userRole.getId(),userRole.getRoleDescription(),userRole.getRoleName());
    }

    @Override
    public List<UserRole> search(String s) throws SQLException, ClassNotFoundException {
        s = "%"+s+"%";
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM user_role WHERE property_id LIKE ? || role_name LIKE ?", s,s);
        List<UserRole> userRoleList = new ArrayList<>();
        while (resultSet.next()){
            userRoleList.add(new UserRole(
                    resultSet.getString(1),resultSet.getString(2),
                    resultSet.getString(3)));
        }
        return userRoleList;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM user_role WHERE property_id=?",s);
    }

    @Override
    public boolean update(UserRole userRole) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE user_role SET role_description=?, role_name=? WHERE property_id=?",
                userRole.getRoleDescription(),userRole.getRoleName(),userRole.getId());
    }

    @Override
    public List<UserRole> loadAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM user_role");
        List<UserRole> userRoleList = new ArrayList<>();
        while (resultSet.next()){
            userRoleList.add(new UserRole(
                    resultSet.getString(1),resultSet.getString(2),resultSet.getString(3)
            ));
        }
        return userRoleList;
    }

    @Override
    public boolean isRoleExist(String roleName) throws SQLException, ClassNotFoundException { // MANAGER
        ResultSet resultSet = CrudUtil.execute("SELECT role_name FROM user_role WHERE role_name=?",roleName);
        return resultSet.next();
    }

    @Override
    public String getIdForLoadManagers() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT property_id FROM user_role WHERE role_name='MANAGER'");
        String managerId = null;
        while (resultSet.next()){
            managerId = resultSet.getString(1);
        }
        return managerId;
    }

    @Override
    public String getUserRoleName(String roleId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet =  CrudUtil.execute("SELECT role_name FROM user_role WHERE property_id=?",roleId);
        String roleName = null;
        while (resultSet.next()){
            roleName = resultSet.getString(1);
        }
        return roleName;
    }
}
