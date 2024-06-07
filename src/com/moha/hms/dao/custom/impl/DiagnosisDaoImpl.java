package com.moha.hms.dao.custom.impl;

import com.moha.hms.dao.CrudUtil;
import com.moha.hms.dao.custom.DiagnosisDao;
import com.moha.hms.dto.DiagnosisDto;
import com.moha.hms.entity.Diagnosis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DiagnosisDaoImpl implements DiagnosisDao {
    @Override
    public boolean create(Diagnosis diagnosis) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO diagnosis VALUES (?,?,?,?,?,?,?,?)",
                diagnosis.getId(), diagnosis.getDescription(), diagnosis.getDate(),diagnosis.getPatientType(),
                diagnosis.getAppointmentDate(),diagnosis.getPatientName(),diagnosis.getDoctorName(),diagnosis.getNurseName());
    }

    @Override
    public List<Diagnosis> search(String id) throws SQLException, ClassNotFoundException {
        id = "%"+id+"%";
        ResultSet resultSet = CrudUtil.execute(
                "SELECT * FROM diagnosis WHERE property_id LIKE ? || patient_property_id Like ? || " +
                        "doctor_property_id LIKE ?",id,id,id);
        List<Diagnosis> diagnosisList = new ArrayList<>();
        while (resultSet.next()){
            diagnosisList.add(new Diagnosis(
                    resultSet.getString(1),resultSet.getString(2),
                    resultSet.getDate(3),resultSet.getString(4),
                    resultSet.getString(5),resultSet.getString(6),
                    resultSet.getString(7),resultSet.getString(8)
            ));
        }
        return diagnosisList;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM diagnosis WHERE property_id=?",id);
    }

    @Override
    public boolean update(Diagnosis diagnosis) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE diagnosis SET " +
                "description=?,date=?,patient_type=?,appointment_property_id=?,patient_property_id=?," +
                        "doctor_property_id=?,nurse_property_id WHERE property_id=?",
                diagnosis.getDescription(), diagnosis.getDate(),diagnosis.getPatientType(),diagnosis.getAppointmentDate(),
                diagnosis.getPatientName(),diagnosis.getDoctorName(),diagnosis.getNurseName(),diagnosis.getId());
    }

    @Override
    public List<Diagnosis> loadAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM diagnosis");
        List<Diagnosis> diagnosisList = new ArrayList<>();
        while (resultSet.next()){
            diagnosisList.add(new Diagnosis(
                    resultSet.getString(1),resultSet.getString(2),
                    resultSet.getDate(3),resultSet.getString(4),
                    resultSet.getString(5),resultSet.getString(6),
                    resultSet.getString(7),resultSet.getString(8)
            ));
        }
        return diagnosisList;
    }

    @Override
    public List<Diagnosis> diagnosedPatient(String patientId) throws SQLException, ClassNotFoundException {
        String query = "SELECT property_id, description, date, patient_type, appointment_property_id, " +
                "patient_property_id, doctor_property_id, nurse_property_id " +
                "FROM diagnosis WHERE patient_property_id=?";
        ResultSet resultSet = CrudUtil.execute(query, patientId);
        List<Diagnosis> diagnosisList = new ArrayList<>();
        while (resultSet.next()) {
            diagnosisList.add(new Diagnosis(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDate(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8)
            ));
        }
        return diagnosisList;
    }
}
