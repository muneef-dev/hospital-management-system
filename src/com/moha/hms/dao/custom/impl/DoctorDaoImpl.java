package com.moha.hms.dao.custom.impl;

import com.moha.hms.dao.CrudUtil;
import com.moha.hms.dao.custom.DoctorDao;
import com.moha.hms.entity.Doctor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorDaoImpl implements DoctorDao {
    @Override
    public boolean create(Doctor doctor) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO doctor VALUES (?,?,?,?,?,?)",
                doctor.getId(), doctor.getName(), doctor.getEmail(),
                doctor.getSpecialistFiled(),doctor.getContactNumber(),doctor.getDepartmentName());
    }

    @Override
    public List<Doctor> search(String id) throws SQLException, ClassNotFoundException {
        id = "%"+id+"%";
        ResultSet resultSet = CrudUtil.execute(
                "SELECT * FROM doctor WHERE property_id LIKE ? || name Like ? || email LIKE ?",id,id,id);
        List<Doctor> doctorList = new ArrayList<>();
        while (resultSet.next()){
            doctorList.add(new Doctor(
                    resultSet.getString(1), resultSet.getString(2),
                    resultSet.getString(3),resultSet.getString(4),
                    resultSet.getString(5),resultSet.getString(6)
            ));
        }
        return doctorList;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM doctor WHERE property_id=?",id);
    }

    @Override
    public boolean update(Doctor doctor) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE doctor SET " +
                "name=?,email=?,specialist=?,contact_number=?,department_property_id=? WHERE property_id=?",
                doctor.getName(), doctor.getEmail(), doctor.getSpecialistFiled(),
                doctor.getContactNumber(),doctor.getDepartmentName(),doctor.getId());
    }

    @Override
    public List<Doctor> loadAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM doctor");
        List<Doctor> doctorList = new ArrayList<>();
        while (resultSet.next()){
            doctorList.add(new Doctor(
                    resultSet.getString(1), resultSet.getString(2),
                    resultSet.getString(3),resultSet.getString(4),
                    resultSet.getString(5),resultSet.getString(6)
            ));
        }
        return doctorList;
    }

    @Override
    public String getDoctorName(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet =  CrudUtil.execute("SELECT name FROM doctor WHERE property_id=?",id);
        String name = null;
        while (resultSet.next()){
            name = resultSet.getString(1);
        }
        return name;
    }
}
