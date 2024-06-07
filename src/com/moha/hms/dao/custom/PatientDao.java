package com.moha.hms.dao.custom;

import com.moha.hms.dao.CrudDao;
import com.moha.hms.entity.Patient;

import java.sql.SQLException;

public interface PatientDao extends CrudDao<Patient,String> {
    String getPatientName(String id) throws SQLException, ClassNotFoundException;
    int getPatientCount() throws SQLException, ClassNotFoundException; // New method
}
