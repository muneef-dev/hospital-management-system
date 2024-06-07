package com.moha.hms.dao.custom;

import com.moha.hms.dao.CrudDao;
import com.moha.hms.entity.Department;
import com.moha.hms.entity.UserRole;

import java.sql.SQLException;

public interface DepartmentDao extends CrudDao<Department,String> {
    String getLastDepartmentId() throws SQLException, ClassNotFoundException;
    String getDepartmentName(String departmentId) throws SQLException, ClassNotFoundException;
}
