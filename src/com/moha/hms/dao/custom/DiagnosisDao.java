package com.moha.hms.dao.custom;

import com.moha.hms.dao.CrudDao;
import com.moha.hms.dto.DiagnosisDto;
import com.moha.hms.entity.Admission;
import com.moha.hms.entity.Diagnosis;

import java.sql.SQLException;
import java.util.List;

public interface DiagnosisDao extends CrudDao<Diagnosis,String> {
    List<Diagnosis> diagnosedPatient(String patientId) throws SQLException, ClassNotFoundException;
}
