package com.moha.hms.bo.custom;

import com.moha.hms.bo.SuperBo;
import com.moha.hms.dto.UserDto;
import com.moha.hms.entity.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserBo extends SuperBo {
    public void initializeSystem();
    boolean createUser(UserDto userDto) throws SQLException, ClassNotFoundException;
    List<UserDto> searchUser(String id) throws SQLException, ClassNotFoundException;
    boolean deleteUser(String id) throws SQLException, ClassNotFoundException;
    boolean updateUser(UserDto userDto) throws SQLException, ClassNotFoundException;
    List<UserDto> loadAllUsers() throws SQLException, ClassNotFoundException;
    List<UserDto> loadAllManagers(String id) throws SQLException, ClassNotFoundException;
    // Method to authenticate user based on email and password
    Optional<UserDto> authenticateUser(String email, String password) throws SQLException, ClassNotFoundException;
    String getUserName(String id) throws SQLException, ClassNotFoundException;
    List<UserDto> searchUserThroughEmail(String emailId) throws SQLException, ClassNotFoundException;


}
