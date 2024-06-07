package com.moha.hms.dao.custom;

import com.moha.hms.dao.CrudDao;
import com.moha.hms.dto.AppointmentDto;
import com.moha.hms.entity.Admission;
import com.moha.hms.entity.Appointment;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface AppointmentDao extends CrudDao<Appointment,String> {
    List<Appointment> isAppointmentExistToThisPatient(String patientId) throws SQLException, ClassNotFoundException;
    int getAppointmentCount() throws SQLException, ClassNotFoundException;
    List<Appointment> getAppointmentsByDoctorAndDate(int doctorId, Date date) throws SQLException, ClassNotFoundException;
}
