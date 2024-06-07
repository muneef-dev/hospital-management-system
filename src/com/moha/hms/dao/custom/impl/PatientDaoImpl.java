package com.moha.hms.dao.custom.impl;

import com.moha.hms.dao.CrudUtil;
import com.moha.hms.dao.custom.PatientDao;
import com.moha.hms.entity.Patient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientDaoImpl implements PatientDao {
    @Override
    public boolean create(Patient patient) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO patient VALUES (?,?,?,?,?,?,?,?,?,?,?)",
                patient.getId(),patient.getNic(),patient.getQrCode(),patient.getName(),
                patient.getDob(),patient.getAge(),patient.getGender(),patient.getAddress(),
                patient.getEmail(),patient.getContactNo(),patient.getEmergencyContactNo());
    }

    @Override
    public List<Patient> search(String id) throws SQLException, ClassNotFoundException {
        id = "%"+id+"%";
        ResultSet resultSet = CrudUtil.execute(
                "SELECT * FROM patient WHERE property_id LIKE ? || name Like ? || email LIKE ?",id,id,id);
        List<Patient> patientList = new ArrayList<>();
        while (resultSet.next()){
            patientList.add(new Patient(
                    resultSet.getString(1), resultSet.getString(2),
                    resultSet.getString(3),resultSet.getString(4),
                    resultSet.getDate(5), resultSet.getString(6),
                    resultSet.getString(7), resultSet.getString(8),
                    resultSet.getString(9), resultSet.getString(10),
                    resultSet.getString(11)
            ));
        }
        return patientList;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM patient WHERE property_id=?",id);
    }

    @Override
    public boolean update(Patient patient) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE patient SET " +
                "nic=?,name=?,dob=?,age=?,gender=?,address=?,email=?,contact_number=?,emergency_contact_number=? WHERE property_id=?",
                patient.getNic(),patient.getName(),patient.getDob(),patient.getAge(),
                patient.getGender(),patient.getAddress(), patient.getEmail(),
                patient.getContactNo(),patient.getEmergencyContactNo(),patient.getId());
    }

    @Override
    public List<Patient> loadAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM patient");
        List<Patient> patientList = new ArrayList<>();
        while (resultSet.next()){
            /*// Assuming Gender is stored as a String in the database
            String genderStr = resultSet.getString(7);

            // Use the utility method to convert the String to Gender enum
            Gender gender = Gender.fromString(genderStr);*/
            patientList.add(new Patient(
                    resultSet.getString(1), resultSet.getString(2),
                    resultSet.getString(3),resultSet.getString(4),
                    resultSet.getDate(5), resultSet.getString(6),
                    resultSet.getString(7), resultSet.getString(8),
                    resultSet.getString(9), resultSet.getString(10),
                    resultSet.getString(11)));
        }
        return patientList;
    }

    @Override
    public String getPatientName(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet =  CrudUtil.execute("SELECT name FROM patient WHERE property_id=?",id);
        String name = null;
        while (resultSet.next()){
            name = resultSet.getString(1);
        }
        return name;
    }

    @Override
    public int getPatientCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT COUNT(*) FROM patient");
        int count = 0;
        if (resultSet.next()) {
            count = resultSet.getInt(1);
        }
        return count;
    }
}
