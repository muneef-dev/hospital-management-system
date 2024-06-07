package com.moha.hms.bo.custom.impl;

import com.moha.hms.bo.custom.AdmissionBo;
import com.moha.hms.dao.DaoFactory;
import com.moha.hms.dao.custom.AdmissionDao;
import com.moha.hms.dto.AdmissionDto;
import com.moha.hms.entity.Admission;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdmissionBoImpl implements AdmissionBo {

    AdmissionDao admissionDao = DaoFactory.getInstance().getDao(DaoFactory.DaoType.ADMISSION);

    @Override
    public boolean createAdmission(AdmissionDto admissionDto) throws SQLException, ClassNotFoundException {
        return admissionDao.create(new Admission(
                admissionDto.getId(), admissionDto.getAdmittingDate(), admissionDto.getDischargeDate(),
                admissionDto.getPatientName(),admissionDto.getRoomName(),admissionDto.getDoctorName(),
                admissionDto.getDiagnosisId()
        ));
    }

    @Override
    public List<AdmissionDto> searchAdmission(String id) throws SQLException, ClassNotFoundException {
        List<AdmissionDto> admissionDtoList = new ArrayList<>();
        for (Admission admission: admissionDao.search(id)
             ) {
            admissionDtoList.add(new AdmissionDto(
                    admission.getId(), admission.getAdmittingDate(), admission.getDischargeDate(),
                    admission.getPatientName(),admission.getRoomName(),admission.getDoctorName(),admission.getDiagnosisId()
            ));
        }
        return admissionDtoList;
    }

    @Override
    public boolean deleteAdmission(String id) throws SQLException, ClassNotFoundException {
        return admissionDao.delete(id);
    }

    @Override
    public boolean updateAdmission(AdmissionDto admissionDto) throws SQLException, ClassNotFoundException {
        return admissionDao.update(new Admission(
                admissionDto.getId(), admissionDto.getAdmittingDate(), admissionDto.getDischargeDate(),
                admissionDto.getPatientName(),admissionDto.getRoomName(),admissionDto.getDoctorName(),admissionDto.getDiagnosisId()
        ));
    }

    @Override
    public List<AdmissionDto> loadAllAdmissions() throws SQLException, ClassNotFoundException {
        List<AdmissionDto> admissionDtoList = new ArrayList<>();
        for (Admission admission : admissionDao.loadAll()
             ) {
            admissionDtoList.add(new AdmissionDto(
                    admission.getId(), admission.getAdmittingDate(), admission.getDischargeDate(),
                    admission.getPatientName(),admission.getRoomName(),admission.getDoctorName(),admission.getDiagnosisId()
            ));
        }
        return admissionDtoList;
    }
}
