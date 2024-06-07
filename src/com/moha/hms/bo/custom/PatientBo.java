package com.moha.hms.bo.custom;

import com.moha.hms.bo.SuperBo;
import com.moha.hms.dto.PatientDto;

import java.sql.SQLException;
import java.util.List;

public interface PatientBo extends SuperBo {
    boolean createPatient(PatientDto patientDto) throws SQLException, ClassNotFoundException;
    List<PatientDto> searchPatient(String id) throws SQLException, ClassNotFoundException;
    boolean deletePatient(String id) throws SQLException, ClassNotFoundException;
    boolean updatePatient(PatientDto patientDto) throws SQLException, ClassNotFoundException;
    List<PatientDto> loadAllPatients() throws SQLException, ClassNotFoundException;
    String getPatientName(String id) throws SQLException, ClassNotFoundException;
    int getPatientCount() throws SQLException, ClassNotFoundException; // New method
}
