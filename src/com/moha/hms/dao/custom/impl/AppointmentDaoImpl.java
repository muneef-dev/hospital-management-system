package com.moha.hms.dao.custom.impl;

import com.moha.hms.dao.CrudUtil;
import com.moha.hms.dao.custom.AppointmentDao;
import com.moha.hms.dto.AppointmentDto;
import com.moha.hms.entity.Appointment;
import com.moha.hms.entity.Diagnosis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

public class AppointmentDaoImpl implements AppointmentDao {
    @Override
    public boolean create(Appointment appointment) throws SQLException, ClassNotFoundException {
        System.out.println(appointment);
        return CrudUtil.execute("INSERT INTO appointment VALUES (?,?,?,?,?,?,?,?)",
                appointment.getId(), appointment.getDate(), appointment.getTime(),appointment.getReason(),
                appointment.getStatus(),appointment.getPatientName(),appointment.getDoctorName(),
                appointment.getUserName());
    }

    @Override
    public List<Appointment> search(String id) throws SQLException, ClassNotFoundException {
        id = "%"+id+"%";
        ResultSet resultSet = CrudUtil.execute(
                "SELECT * FROM appointment WHERE property_id LIKE ? || patient_property_id Like ? || doctor_property_id LIKE ?",id,id,id);
        List<Appointment> appointmentList = new ArrayList<>();
        while (resultSet.next()){
            appointmentList.add(new Appointment(
                    resultSet.getString(1),resultSet.getDate(2),
                    resultSet.getTime(3),resultSet.getString(4),
                    resultSet.getString(5),resultSet.getString(6),
                    resultSet.getString(7),resultSet.getString(8)
            ));
        }
        return appointmentList;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM appointment WHERE property_id=?",id);
    }

    @Override
    public boolean update(Appointment appointment) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE appointment SET " +
                "date=?,time=?,reason=?,status=?,patient_property_id=?,doctor_property_id=?,user_property_id WHERE property_id=?",
                appointment.getDate(), appointment.getTime(),appointment.getReason(),
                appointment.getStatus(),appointment.getPatientName(),appointment.getDoctorName(),
                appointment.getUserName(),appointment.getId());
    }

    @Override
    public List<Appointment> loadAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM appointment");
        List<Appointment> appointmentList = new ArrayList<>();
        while (resultSet.next()){
            appointmentList.add(new Appointment(
                    resultSet.getString(1),resultSet.getDate(2),
                    resultSet.getTime(3),resultSet.getString(4),
                    resultSet.getString(5),resultSet.getString(6),
                    resultSet.getString(7),resultSet.getString(8)
            ));
        }
        return appointmentList;
    }

//    @Override
//    public List<Appointment> isAppointmentExistToThisPatient(String patientId) throws SQLException, ClassNotFoundException {
//        ResultSet resultSet = CrudUtil.execute("SELECT patient_property_id FROM appointment WHERE patient_property_id=?",patientId);
//        List<Appointment> appointmentList = new ArrayList<>();
//        while (resultSet.next()){
//            appointmentList.add(new Appointment(
//                    resultSet.getString(1),resultSet.getDate(2),
//                    resultSet.getTime(3),resultSet.getString(4),
//                    resultSet.getString(5),resultSet.getString(6),
//                    resultSet.getString(7),resultSet.getString(8)
//            ));
//        }
//        return appointmentList;
//    }

    @Override
    public List<Appointment> isAppointmentExistToThisPatient(String patientId) throws SQLException, ClassNotFoundException {
        String query = "SELECT property_id, date, time, description, patient_property_id, " +
                "doctor_property_id, nurse_property_id, status " +
                "FROM appointment WHERE patient_property_id=?";
        ResultSet resultSet = CrudUtil.execute(query, patientId);
        List<Appointment> appointmentList = new ArrayList<>();
        while (resultSet.next()) {
            appointmentList.add(new Appointment(
                    resultSet.getString(1),resultSet.getDate(2),
                    resultSet.getTime(3),resultSet.getString(4),
                    resultSet.getString(5),resultSet.getString(6),
                    resultSet.getString(7),resultSet.getString(8)
            ));
        }
        return appointmentList;
    }

    @Override
    public int getAppointmentCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT COUNT(*) FROM appointment");
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    @Override
    public List<Appointment> getAppointmentsByDoctorAndDate(int doctorId, Date date) throws SQLException, ClassNotFoundException {
        String query = "SELECT * FROM appointment WHERE doctor_property_id = ? AND date = ?";
        ResultSet resultSet = CrudUtil.execute(query, doctorId, date);
        List<Appointment> appointmentList = new ArrayList<>();
        while (resultSet.next()) {
            appointmentList.add(new Appointment(
                    resultSet.getString(1), resultSet.getDate(2),
                    resultSet.getTime(3), resultSet.getString(4),
                    resultSet.getString(5), resultSet.getString(6),
                    resultSet.getString(7), resultSet.getString(8)
            ));
        }
        return appointmentList;
    }
}
