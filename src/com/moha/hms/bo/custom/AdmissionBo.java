package com.moha.hms.bo.custom;

import com.moha.hms.bo.SuperBo;
import com.moha.hms.dto.AdmissionDto;

import java.sql.SQLException;
import java.util.List;

public interface AdmissionBo extends SuperBo {
    boolean createAdmission(AdmissionDto admissionDto) throws SQLException, ClassNotFoundException;
    List<AdmissionDto> searchAdmission(String id) throws SQLException, ClassNotFoundException;
    boolean deleteAdmission(String id) throws SQLException, ClassNotFoundException;
    boolean updateAdmission(AdmissionDto admissionDto) throws SQLException, ClassNotFoundException;
    List<AdmissionDto> loadAllAdmissions() throws SQLException, ClassNotFoundException;
}
