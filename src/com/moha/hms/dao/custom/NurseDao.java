package com.moha.hms.dao.custom;

import com.moha.hms.dao.CrudDao;
import com.moha.hms.entity.Doctor;
import com.moha.hms.entity.Nurse;

import java.sql.SQLException;

public interface NurseDao extends CrudDao<Nurse,String> {
    String getNurseName(String id) throws SQLException, ClassNotFoundException;
}
