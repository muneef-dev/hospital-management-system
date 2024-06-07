package com.moha.hms.dao.custom.impl;

import com.moha.hms.dao.CrudUtil;
import com.moha.hms.dao.custom.UserDao;
import com.moha.hms.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    @Override
    public boolean create(User user) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO user VALUES (?,?,?,?,?)",
                user.getId(), user.getEmail(), user.getName(), user.getPassword(), user.getRole());
    }

    @Override
    public List<User> search(String id) throws SQLException, ClassNotFoundException {
        id = "%"+id+"%";
        ResultSet resultSet = CrudUtil.execute(
                "SELECT * FROM user WHERE property_id LIKE ? || name Like ? || email LIKE ?",id,id,id);
        List<User> userList = new ArrayList<>();
        while (resultSet.next()){
            userList.add(new User(
                    resultSet.getString(1), resultSet.getString(2),
                    resultSet.getString(3),resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }
        return userList;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM user WHERE property_id=?",id);
    }

    @Override
    public boolean update(User user) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE user SET " +
                "email=?,name=?,password=?,user_role_property_id=? WHERE property_id=?",
                user.getEmail(),user.getName(),user.getPassword(),user.getRole(),user.getId());
    }

    @Override
    public List<User> loadAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM user");
        List<User> userList = new ArrayList<>();
        while (resultSet.next()){
            userList.add(new User(
                    resultSet.getString(1), resultSet.getString(2),
                    resultSet.getString(3),resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }
        return userList;
    }

    @Override
    public List<User> loadAllManagers(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT name FROM user WHERE user_role_property_id=?",id);
        List<User> userList = new ArrayList<>();
        while(resultSet.next()){
            userList.add(new User(
                    null, null,
                    resultSet.getString(1), null,
                    null
            ));
        }
        return userList;
    }

    @Override
    public Optional<User> getUserByEmail(String email) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM user WHERE email=?", email);
        if (resultSet.next()) {
            User user = new User(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public String getUserName(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet =  CrudUtil.execute("SELECT name FROM user WHERE property_id=?",id);
        String name = null;
        while (resultSet.next()){
            name = resultSet.getString(1);
        }
        return name;
    }

    @Override
    public List<User> searchUserThroughEmail(String emailId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute(
                "SELECT * FROM user WHERE email=?",emailId);
        List<User> userList = new ArrayList<>();
        while (resultSet.next()){
            userList.add(new User(
                    resultSet.getString(1), resultSet.getString(2),
                    resultSet.getString(3),resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }
        return userList;
    }
}
