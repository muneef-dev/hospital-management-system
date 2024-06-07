package com.moha.hms.dao.custom;

import com.moha.hms.dao.CrudDao;
import com.moha.hms.dto.UserDto;
import com.moha.hms.entity.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserDao extends CrudDao<User,String> {
    List<User> loadAllManagers(String id) throws SQLException, ClassNotFoundException;
    Optional<User> getUserByEmail(String email) throws SQLException, ClassNotFoundException;
    String getUserName(String id) throws SQLException, ClassNotFoundException;
    List<User> searchUserThroughEmail(String emailId) throws SQLException, ClassNotFoundException;
}
