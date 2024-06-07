package com.moha.hms.dao.custom.impl;

import com.moha.hms.dao.CrudUtil;
import com.moha.hms.dao.custom.AdmissionDao;
import com.moha.hms.entity.Admission;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdmissionDaoImpl implements AdmissionDao {
    @Override
    public boolean create(Admission admission) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO admission VALUES (?,?,?,?,?,?,?)",
                admission.getId(), admission.getAdmittingDate(), admission.getDischargeDate(),
                admission.getPatientName(),admission.getRoomName(),admission.getDoctorName(),admission.getDiagnosisId());
    }

    @Override
    public List<Admission> search(String id) throws SQLException, ClassNotFoundException {
        id = "%"+id+"%";
        ResultSet resultSet = CrudUtil.execute(
                "SELECT * FROM admission WHERE property_id LIKE ? || patient_property_id Like ? || diagnosis_property_id LIKE ?",id,id,id);
        List<Admission> admissionList = new ArrayList<>();
        while (resultSet.next()){
            admissionList.add(new Admission(
                    resultSet.getString(1),resultSet.getDate(2),
                    resultSet.getDate(3),resultSet.getString(4),
                    resultSet.getString(5),resultSet.getString(6),
                    resultSet.getString(7)
            ));
        }
        return admissionList;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM admission WHERE property_id=?",id);
    }

    @Override
    public boolean update(Admission admission) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE admission SET " +
                "admitting_date=?,discharge_date=?,patient_property_id=?,room_property_id=?,doctor_property_id=?,diagnosis_property_id WHERE property_id=?",
                admission.getAdmittingDate(), admission.getDischargeDate(), admission.getPatientName(),
                admission.getRoomName(),admission.getDoctorName(),admission.getDiagnosisId(),admission.getId());
    }

    @Override
    public List<Admission> loadAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM admission");
        List<Admission> admissionList = new ArrayList<>();
        while (resultSet.next()){
            admissionList.add(new Admission(
                    resultSet.getString(1),resultSet.getDate(2),
                    resultSet.getDate(3),resultSet.getString(4),
                    resultSet.getString(5),resultSet.getString(6),
                    resultSet.getString(7)
            ));
        }
        return admissionList;
    }
}
