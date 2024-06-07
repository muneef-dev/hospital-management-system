package com.moha.hms.bo.custom.impl;

import com.moha.hms.bo.custom.DiagnosisBo;
import com.moha.hms.dao.DaoFactory;
import com.moha.hms.dao.custom.DiagnosisDao;
import com.moha.hms.dto.DiagnosisDto;
import com.moha.hms.entity.Diagnosis;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DiagnosisBoImpl implements DiagnosisBo {

    DiagnosisDao diagnosisDao = DaoFactory.getInstance().getDao(DaoFactory.DaoType.DIAGNOSIS);

    @Override
    public boolean createDiagnosis(DiagnosisDto diagnosisDto) throws SQLException, ClassNotFoundException {
        return diagnosisDao.create(new Diagnosis(
                diagnosisDto.getId(), diagnosisDto.getDescription(), diagnosisDto.getDate(),diagnosisDto.getPatientType(),
                diagnosisDto.getAppointmentId(),diagnosisDto.getPatientName(),diagnosisDto.getDoctorName(),
                diagnosisDto.getNurseName()
        ));
    }

    @Override
    public List<DiagnosisDto> searchDiagnosis(String id) throws SQLException, ClassNotFoundException {
        List<DiagnosisDto> diagnosisDtoList = new ArrayList<>();
        for (Diagnosis diagnosis: diagnosisDao.search(id)
             ) {
            diagnosisDtoList.add(new DiagnosisDto(
                    diagnosis.getId(), diagnosis.getDescription(), diagnosis.getDate(),diagnosis.getPatientType(),
                    diagnosis.getAppointmentDate(),diagnosis.getPatientName(),diagnosis.getDoctorName(),diagnosis.getNurseName()
            ));
        }
        return diagnosisDtoList;
    }

    @Override
    public boolean deleteDiagnosis(String id) throws SQLException, ClassNotFoundException {
        return diagnosisDao.delete(id);
    }

    @Override
    public boolean updateDiagnosis(DiagnosisDto diagnosisDto) throws SQLException, ClassNotFoundException {
        return diagnosisDao.update(new Diagnosis(
                diagnosisDto.getId(), diagnosisDto.getDescription(), diagnosisDto.getDate(),diagnosisDto.getPatientType(),
                diagnosisDto.getAppointmentId(),diagnosisDto.getPatientName(),diagnosisDto.getDoctorName(),
                diagnosisDto.getNurseName()
        ));
    }

    @Override
    public List<DiagnosisDto> loadAllDiagnosiss() throws SQLException, ClassNotFoundException {
        List<DiagnosisDto> diagnosisDtoList = new ArrayList<>();
        for (Diagnosis diagnosis : diagnosisDao.loadAll()
             ) {
            diagnosisDtoList.add(new DiagnosisDto(
                    diagnosis.getId(), diagnosis.getDescription(), diagnosis.getDate(),diagnosis.getPatientType(),
                    diagnosis.getAppointmentDate(),diagnosis.getPatientName(),diagnosis.getDoctorName(),diagnosis.getNurseName()
            ));
        }
        return diagnosisDtoList;
    }

    @Override
    public List<DiagnosisDto> diagnosedPatient(String patientId) throws SQLException, ClassNotFoundException {
        List<DiagnosisDto> diagnosisDtoList = new ArrayList<>();
        for (Diagnosis diagnosis: diagnosisDao.diagnosedPatient(patientId)
        ) {
            diagnosisDtoList.add(new DiagnosisDto(
                    diagnosis.getId(), diagnosis.getDescription(), diagnosis.getDate(),diagnosis.getPatientType(),
                    diagnosis.getAppointmentDate(),diagnosis.getPatientName(),diagnosis.getDoctorName(),diagnosis.getNurseName()
            ));
        }
        return diagnosisDtoList;
    }
}
