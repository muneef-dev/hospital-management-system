package com.moha.hms.bo.custom;

import com.moha.hms.bo.SuperBo;
import com.moha.hms.dto.MedicationDto;
import com.moha.hms.entity.Medication;

import java.sql.SQLException;
import java.util.List;

public interface MedicationBo extends SuperBo {
    boolean createMedication(MedicationDto medicationDto) throws SQLException, ClassNotFoundException;
    List<MedicationDto> searchMedication(String id) throws SQLException, ClassNotFoundException;
    boolean deleteMedication(String id) throws SQLException, ClassNotFoundException;
    boolean updateMedication(MedicationDto medicationDto) throws SQLException, ClassNotFoundException;
    List<MedicationDto> loadAllMedications() throws SQLException, ClassNotFoundException;
    public List<MedicationDto> isMedicationAvailableForPatient(String diagnosisId) throws SQLException, ClassNotFoundException;
}
