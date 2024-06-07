package com.moha.hms.bo.custom;

import com.moha.hms.bo.SuperBo;
import com.moha.hms.dto.UserDto;
import com.moha.hms.dto.UserRoleDto;

import java.sql.SQLException;
import java.util.List;

public interface UserRoleBo extends SuperBo {
    boolean createUserRole(UserRoleDto userRoleDto) throws SQLException, ClassNotFoundException;
    List<UserRoleDto> searchUserRole(String id) throws SQLException, ClassNotFoundException;
    boolean deleteUserRole(String id) throws SQLException, ClassNotFoundException;
    boolean updateUserRole(UserRoleDto userRoleDto) throws SQLException, ClassNotFoundException;
    List<UserRoleDto> loadAllUserRoles() throws SQLException, ClassNotFoundException;
    boolean isRoleExist(String roleName) throws SQLException, ClassNotFoundException;
    String getIdForLoadManagers() throws SQLException, ClassNotFoundException;
    String getUserRoleName(String roleId) throws SQLException, ClassNotFoundException;
}
