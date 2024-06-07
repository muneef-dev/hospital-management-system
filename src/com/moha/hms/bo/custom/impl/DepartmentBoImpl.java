package com.moha.hms.bo.custom.impl;

import com.moha.hms.bo.custom.DepartmentBo;
import com.moha.hms.dao.DaoFactory;
import com.moha.hms.dao.custom.DepartmentDao;
import com.moha.hms.dto.DepartmentDto;
import com.moha.hms.entity.Department;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentBoImpl implements DepartmentBo {

    DepartmentDao departmentDao = DaoFactory.getInstance().getDao(DaoFactory.DaoType.DEPARTMENT);
    @Override
    public boolean createDepartment(DepartmentDto department) throws SQLException, ClassNotFoundException {
        return departmentDao.create(new Department(
                department.getId(),department.getName(),department.getFloor(),department.getManagerName()));
    }

    @Override
    public List<DepartmentDto> searchDepartment(String id) throws SQLException, ClassNotFoundException {
        List<DepartmentDto> departmentList = new ArrayList<>();
        for (Department department : departmentDao.search(id)) {
            departmentList.add(new DepartmentDto(
                    department.getId(),department.getName(),department.getFloor(),department.getManagerName()
            ));
        }
        return departmentList;
    }

    @Override
    public boolean deleteDepartment(String id) throws SQLException, ClassNotFoundException {
        return departmentDao.delete(id);
    }

    @Override
    public boolean updateDepartment(DepartmentDto department) throws SQLException, ClassNotFoundException {
        return departmentDao.update(new Department(
                department.getId(),department.getName(),department.getFloor(),department.getManagerName()));
    }

    @Override
    public List<DepartmentDto> loadAllDepartments() throws SQLException, ClassNotFoundException {
        List<DepartmentDto> departmentList = new ArrayList<>();
        for (Department department : departmentDao.loadAll()) {
            departmentList.add(new DepartmentDto(
                    department.getId(),department.getName(),department.getFloor(),department.getManagerName()
            ));
        }
        return departmentList;
    }

    @Override
    public String getLastDepartmentId() throws SQLException, ClassNotFoundException {
        return departmentDao.getLastDepartmentId();
    }

    @Override
    public String getDepartmentName(String departmentId) throws SQLException, ClassNotFoundException {
        return departmentDao.getDepartmentName(departmentId);
    }
}
