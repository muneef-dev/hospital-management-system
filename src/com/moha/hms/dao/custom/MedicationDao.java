package com.moha.hms.dao.custom;

import com.moha.hms.dao.CrudDao;
import com.moha.hms.entity.Medication;

import java.sql.SQLException;
import java.util.List;

public interface MedicationDao extends CrudDao<Medication,String> {
    List<Medication> isMedicationAvailableForPatient(String diagnosisId) throws SQLException, ClassNotFoundException;
}
