package com.moha.hms.bo.custom.impl;

import com.moha.hms.bo.custom.PatientBo;
import com.moha.hms.dao.DaoFactory;
import com.moha.hms.dao.SuperDao;
import com.moha.hms.dao.custom.PatientDao;
import com.moha.hms.dto.PatientDto;
import com.moha.hms.entity.Patient;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientBoImpl implements PatientBo {

    PatientDao patientDao = DaoFactory.getInstance().getDao(DaoFactory.DaoType.PATIENT);

    @Override
    public boolean createPatient(PatientDto patientDto) throws SQLException, ClassNotFoundException {
        return patientDao.create(new Patient(
                patientDto.getId(), patientDto.getNic(), patientDto.getQrCode(),
                patientDto.getName(), patientDto.getDob(), patientDto.getAge(),
                patientDto.getGender(), patientDto.getAddress(), patientDto.getEmail(),
                patientDto.getContactNo(), patientDto.getEmergencyContactNo()
        ));
    }

    @Override
    public List<PatientDto> searchPatient(String id) throws SQLException, ClassNotFoundException {
        List<PatientDto> patientDtoList = new ArrayList<>();
        for (Patient patient: patientDao.search(id)
             ) {
            patientDtoList.add(new PatientDto(
                    patient.getId(), patient.getNic(),
                    patient.getQrCode(), patient.getName(),
                    patient.getDob(), patient.getAge(),
                    patient.getGender(), patient.getAddress(),
                    patient.getEmail(), patient.getContactNo(),
                    patient.getEmergencyContactNo()
            ));
        }
        return patientDtoList;
    }

    @Override
    public boolean deletePatient(String id) throws SQLException, ClassNotFoundException {
        return patientDao.delete(id);
    }

    @Override
    public boolean updatePatient(PatientDto patientDto) throws SQLException, ClassNotFoundException {
        return patientDao.update(new Patient(
                patientDto.getId(), patientDto.getNic(),
                patientDto.getQrCode(), patientDto.getName(),
                patientDto.getDob(), patientDto.getAge(),
                patientDto.getGender(), patientDto.getAddress(),
                patientDto.getEmail(), patientDto.getContactNo(),
                patientDto.getEmergencyContactNo()
        ));
    }

    @Override
    public List<PatientDto> loadAllPatients() throws SQLException, ClassNotFoundException {
        List<PatientDto> patientDtoList = new ArrayList<>();
        for (Patient patient : patientDao.loadAll()
             ) {
            patientDtoList.add(new PatientDto(
                    patient.getId(), patient.getNic(),
                    patient.getQrCode(), patient.getName(),
                    patient.getDob(), patient.getAge(),
                    patient.getGender(), patient.getAddress(),
                    patient.getEmail(), patient.getContactNo(),
                    patient.getEmergencyContactNo()
            ));
        }
        return patientDtoList;
    }

    @Override
    public String getPatientName(String id) throws SQLException, ClassNotFoundException {
        return patientDao.getPatientName(id);
    }

    @Override
    public int getPatientCount() throws SQLException, ClassNotFoundException {
        return patientDao.getPatientCount();
    }
}
