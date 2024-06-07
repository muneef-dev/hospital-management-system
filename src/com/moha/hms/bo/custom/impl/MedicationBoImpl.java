package com.moha.hms.bo.custom.impl;

import com.moha.hms.bo.custom.MedicationBo;
import com.moha.hms.dao.DaoFactory;
import com.moha.hms.dao.custom.MedicationDao;
import com.moha.hms.dto.MedicationDto;
import com.moha.hms.entity.Medication;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicationBoImpl implements MedicationBo {

    MedicationDao medicationDao = DaoFactory.getInstance().getDao(DaoFactory.DaoType.MEDICATION);

    @Override
    public boolean createMedication(MedicationDto medicationDto) throws SQLException, ClassNotFoundException {
        return medicationDao.create(new Medication(
                medicationDto.getId(), medicationDto.getPatientId(), medicationDto.getPatientName(),
                medicationDto.getSoldQty(), medicationDto.getTotalCost(),
                medicationDto.getDiagnosisId(), medicationDto.getInventoryItemId()
        ));
    }

    @Override
    public List<MedicationDto> searchMedication(String id) throws SQLException, ClassNotFoundException {
        List<MedicationDto> medicationDtoList = new ArrayList<>();
        for (Medication medication : medicationDao.search(id)) {
            medicationDtoList.add(new MedicationDto(
                    medication.getId(), medication.getPatientId(), medication.getPatientName(),
                    medication.getSoldQty(), medication.getTotalCost(),
                    medication.getDiagnosisId(), medication.getInventoryItemId()
            ));
        }
        return medicationDtoList;
    }

    @Override
    public boolean deleteMedication(String id) throws SQLException, ClassNotFoundException {
        return medicationDao.delete(id);
    }

    @Override
    public boolean updateMedication(MedicationDto medicationDto) throws SQLException, ClassNotFoundException {
        return medicationDao.update(new Medication(
                medicationDto.getId(), medicationDto.getPatientId(), medicationDto.getPatientName(),
                medicationDto.getSoldQty(), medicationDto.getTotalCost(),
                medicationDto.getDiagnosisId(), medicationDto.getInventoryItemId()
        ));
    }

    @Override
    public List<MedicationDto> loadAllMedications() throws SQLException, ClassNotFoundException {
        List<MedicationDto> medicationDtoList = new ArrayList<>();
        for (Medication medication : medicationDao.loadAll()) {
            medicationDtoList.add(new MedicationDto(
                    medication.getId(), medication.getPatientId(), medication.getPatientName(),
                    medication.getSoldQty(), medication.getTotalCost(),
                    medication.getDiagnosisId(), medication.getInventoryItemId()
            ));
        }
        return medicationDtoList;
    }

    @Override
    public List<MedicationDto> isMedicationAvailableForPatient(String diagnosisId) throws SQLException, ClassNotFoundException {
        List<MedicationDto> medicationDtoList = new ArrayList<>();
        for (Medication medication : medicationDao.isMedicationAvailableForPatient(diagnosisId)) {
            medicationDtoList.add(new MedicationDto(
                    medication.getId(), medication.getPatientId(), medication.getPatientName(),
                    medication.getSoldQty(), medication.getTotalCost(),
                    medication.getDiagnosisId(), medication.getInventoryItemId()
            ));
        }
        return medicationDtoList;
    }
}
