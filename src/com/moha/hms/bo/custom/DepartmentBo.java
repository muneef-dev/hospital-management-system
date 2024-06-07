package com.moha.hms.bo.custom;

import com.moha.hms.bo.SuperBo;
import com.moha.hms.dto.DepartmentDto;
import com.moha.hms.dto.UserRoleDto;

import java.sql.SQLException;
import java.util.List;

public interface DepartmentBo extends SuperBo {
    boolean createDepartment(DepartmentDto departmentDto) throws SQLException, ClassNotFoundException;
    List<DepartmentDto> searchDepartment(String id) throws SQLException, ClassNotFoundException;
    boolean deleteDepartment(String id) throws SQLException, ClassNotFoundException;
    boolean updateDepartment(DepartmentDto departmentDto) throws SQLException, ClassNotFoundException;
    List<DepartmentDto> loadAllDepartments() throws SQLException, ClassNotFoundException;
    String getLastDepartmentId() throws SQLException, ClassNotFoundException;
    String getDepartmentName(String departmentId) throws SQLException, ClassNotFoundException;
}
