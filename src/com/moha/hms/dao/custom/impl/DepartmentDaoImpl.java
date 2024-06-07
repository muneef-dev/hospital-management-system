package com.moha.hms.dao.custom.impl;

import com.moha.hms.dao.CrudUtil;
import com.moha.hms.dao.custom.DepartmentDao;
import com.moha.hms.dao.custom.DepartmentDao;
import com.moha.hms.entity.Department;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoImpl implements DepartmentDao {
    @Override
    public boolean create(Department department) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO department VALUES (?,?,?,?)",
                department.getId(),department.getName(),department.getFloor(),department.getManagerName());
    }

    @Override
    public List<Department> search(String s) throws SQLException, ClassNotFoundException {
        s = "%"+s+"%";
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM department WHERE property_id LIKE ? || name LIKE ? || manager LIKE ?",s,s,s);
        List<Department> departmentList = new ArrayList<>();
        while (resultSet.next()){
            departmentList.add(new Department(
                    resultSet.getString(1),resultSet.getString(2),
                    resultSet.getInt(3),resultSet.getString(4)));
        }
        return departmentList;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM department WHERE property_id=?",s);
    }

    @Override
    public boolean update(Department department) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE department SET name=?, floor=?, manager=? WHERE property_id=?",
                department.getName(),department.getFloor(),department.getManagerName(),department.getId());
    }

    @Override
    public List<Department> loadAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM department");
        List<Department> departmentList = new ArrayList<>();
        while (resultSet.next()){
            departmentList.add(new Department(
                    resultSet.getString(1),resultSet.getString(2),
                    resultSet.getInt(3),resultSet.getString(4)
            ));
        }
        return departmentList;
    }

    @Override
    public String getLastDepartmentId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute(
                "SELECT property_id FROM department ORDER BY CAST(SUBSTRING(property_id,3)AS UNSIGNED) DESC LIMIT 1");
        if (resultSet.next()){
            return resultSet.getString(1);
        }
        return null;
    }

    @Override
    public String getDepartmentName(String departmentId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet =  CrudUtil.execute("SELECT name FROM department WHERE property_id=?",departmentId);
        String departmentName = null;
        while (resultSet.next()){
            departmentName = resultSet.getString(1);
        }
        return departmentName;
    }
}
