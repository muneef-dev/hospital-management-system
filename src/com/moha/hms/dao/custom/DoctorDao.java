package com.moha.hms.dao.custom;

import com.moha.hms.dao.CrudDao;
import com.moha.hms.entity.Doctor;
import com.moha.hms.entity.User;

import java.sql.SQLException;
import java.util.List;

public interface DoctorDao extends CrudDao<Doctor,String> {
    String getDoctorName(String id) throws SQLException, ClassNotFoundException;
}
