package com.moha.hms.bo.custom.impl;

import com.moha.hms.bo.custom.UserRoleBo;
import com.moha.hms.dao.DaoFactory;
import com.moha.hms.dao.custom.UserRoleDao;
import com.moha.hms.dto.UserRoleDto;
import com.moha.hms.entity.UserRole;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRoleBoImpl implements UserRoleBo {

    UserRoleDao userRoleDao = DaoFactory.getInstance().getDao(DaoFactory.DaoType.USER_ROLE);
    @Override
    public boolean createUserRole(UserRoleDto userRoleDto) throws SQLException, ClassNotFoundException {
        return userRoleDao.create(new UserRole(
                userRoleDto.getId(),userRoleDto.getRoleDescription(),userRoleDto.getRoleName()));
    }

    @Override
    public List<UserRoleDto> searchUserRole(String id) throws SQLException, ClassNotFoundException {
        List<UserRoleDto> userRolesList = new ArrayList<>();
        for (UserRole userRole : userRoleDao.search(id)) {
            userRolesList.add(new UserRoleDto(
                    userRole.getId(),userRole.getRoleDescription(), userRole.getRoleName()
            ));
        }
        return userRolesList;
    }

    @Override
    public boolean deleteUserRole(String id) throws SQLException, ClassNotFoundException {
        return userRoleDao.delete(id);
    }

    @Override
    public boolean updateUserRole(UserRoleDto userRoleDto) throws SQLException, ClassNotFoundException {
        return userRoleDao.update(new UserRole(
                userRoleDto.getId(),userRoleDto.getRoleDescription(),userRoleDto.getRoleName()));
    }

    @Override
    public List<UserRoleDto> loadAllUserRoles() throws SQLException, ClassNotFoundException {
        List<UserRoleDto> userRoleDtos = new ArrayList<>();
        for (UserRole userRole : userRoleDao.loadAll()) {
            userRoleDtos.add(new UserRoleDto(
                    userRole.getId(),userRole.getRoleDescription(), userRole.getRoleName()
            ));
        }
        return userRoleDtos;
    }

    @Override
    public boolean isRoleExist(String roleName) throws SQLException, ClassNotFoundException {
        return userRoleDao.isRoleExist(roleName);
    }

    @Override
    public String getIdForLoadManagers() throws SQLException, ClassNotFoundException {
        return userRoleDao.getIdForLoadManagers();
    }

    @Override
    public String getUserRoleName(String roleId) throws SQLException, ClassNotFoundException {
        return userRoleDao.getUserRoleName(roleId);
    }
}
