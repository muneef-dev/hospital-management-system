package com.moha.hms.bo.custom;

import com.moha.hms.bo.SuperBo;
import com.moha.hms.dto.DiagnosisDto;

import java.sql.SQLException;
import java.util.List;

public interface DiagnosisBo extends SuperBo {
    boolean createDiagnosis(DiagnosisDto diagnosisDto) throws SQLException, ClassNotFoundException;
    List<DiagnosisDto> searchDiagnosis(String id) throws SQLException, ClassNotFoundException;
    boolean deleteDiagnosis(String id) throws SQLException, ClassNotFoundException;
    boolean updateDiagnosis(DiagnosisDto diagnosisDto) throws SQLException, ClassNotFoundException;
    List<DiagnosisDto> loadAllDiagnosiss() throws SQLException, ClassNotFoundException;
    List<DiagnosisDto> diagnosedPatient(String patientId) throws SQLException, ClassNotFoundException;

}
