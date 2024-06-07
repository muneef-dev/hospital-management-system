package com.moha.hms.dao.custom;

import com.moha.hms.dao.CrudDao;
import com.moha.hms.entity.User;
import com.moha.hms.entity.UserRole;

import java.sql.SQLException;

public interface UserRoleDao extends CrudDao<UserRole,String> {
    boolean isRoleExist(String roleName) throws SQLException, ClassNotFoundException;
    String getIdForLoadManagers() throws SQLException, ClassNotFoundException;
    String getUserRoleName(String roleId) throws SQLException, ClassNotFoundException;
}
