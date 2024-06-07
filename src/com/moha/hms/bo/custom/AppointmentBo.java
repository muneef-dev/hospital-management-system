package com.moha.hms.bo.custom;

import com.moha.hms.bo.SuperBo;
import com.moha.hms.dto.AppointmentDto;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface AppointmentBo extends SuperBo {
    boolean createAppointment(AppointmentDto appointmentDto) throws SQLException, ClassNotFoundException;
    List<AppointmentDto> searchAppointment(String id) throws SQLException, ClassNotFoundException;
    boolean deleteAppointment(String id) throws SQLException, ClassNotFoundException;
    boolean updateAppointment(AppointmentDto appointmentDto) throws SQLException, ClassNotFoundException;
    List<AppointmentDto> loadAllAppointments() throws SQLException, ClassNotFoundException;
    List<AppointmentDto> isAppointmentExistToThisPatient(String patientId) throws SQLException, ClassNotFoundException;
    int getAppointmentCount() throws SQLException, ClassNotFoundException;
    List<AppointmentDto> getAppointmentsByDoctorAndDate(int doctorId, Date date) throws SQLException, ClassNotFoundException;
}
