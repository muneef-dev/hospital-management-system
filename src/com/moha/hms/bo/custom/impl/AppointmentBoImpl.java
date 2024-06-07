package com.moha.hms.bo.custom.impl;

import com.moha.hms.bo.custom.AppointmentBo;
import com.moha.hms.dao.DaoFactory;
import com.moha.hms.dao.custom.AppointmentDao;
import com.moha.hms.dto.AppointmentDto;
import com.moha.hms.entity.Appointment;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AppointmentBoImpl implements AppointmentBo {

    AppointmentDao appointmentDao = DaoFactory.getInstance().getDao(DaoFactory.DaoType.APPOINTMENT);

    @Override
    public boolean createAppointment(AppointmentDto appointmentDto) throws SQLException, ClassNotFoundException {
        return appointmentDao.create(new Appointment(
                appointmentDto.getId(), appointmentDto.getDate(), appointmentDto.getTime(),
                appointmentDto.getPatientName(),appointmentDto.getDoctorName(),
                appointmentDto.getReason(), appointmentDto.getStatus(), appointmentDto.getUserName()
        ));
    }

    @Override
    public List<AppointmentDto> searchAppointment(String id) throws SQLException, ClassNotFoundException {
        List<AppointmentDto> appointmentDtoList = new ArrayList<>();
        for (Appointment appointment: appointmentDao.search(id)
             ) {
            appointmentDtoList.add(new AppointmentDto(
                    appointment.getId(), appointment.getDate(), appointment.getTime(),appointment.getReason(),
                    appointment.getStatus(),appointment.getPatientName(),appointment.getDoctorName(),
                    appointment.getUserName()
            ));
        }
        return appointmentDtoList;
    }

    @Override
    public boolean deleteAppointment(String id) throws SQLException, ClassNotFoundException {
        return appointmentDao.delete(id);
    }

    @Override
    public boolean updateAppointment(AppointmentDto appointmentDto) throws SQLException, ClassNotFoundException {
        return appointmentDao.update(new Appointment(
                appointmentDto.getId(), appointmentDto.getDate(), appointmentDto.getTime(),appointmentDto.getReason(),
                appointmentDto.getStatus(),appointmentDto.getPatientName(),appointmentDto.getDoctorName(),
                appointmentDto.getUserName()
        ));
    }

    @Override
    public List<AppointmentDto> loadAllAppointments() throws SQLException, ClassNotFoundException {
        List<AppointmentDto> appointmentDtoList = new ArrayList<>();
        for (Appointment appointment : appointmentDao.loadAll()
             ) {
            appointmentDtoList.add(new AppointmentDto(
                    appointment.getId(), appointment.getDate(), appointment.getTime(),appointment.getReason(),
                    appointment.getStatus(),appointment.getPatientName(),appointment.getDoctorName(),
                    appointment.getUserName()
            ));
        }
        return appointmentDtoList;
    }

    @Override
    public List<AppointmentDto> isAppointmentExistToThisPatient(String patientId) throws SQLException, ClassNotFoundException {
        List<AppointmentDto> appointmentDtoList = new ArrayList<>();
        for (Appointment appointment : appointmentDao.isAppointmentExistToThisPatient(patientId)
        ) {
            appointmentDtoList.add(new AppointmentDto(
                    appointment.getId(), appointment.getDate(), appointment.getTime(),appointment.getReason(),
                    appointment.getStatus(),appointment.getPatientName(),appointment.getDoctorName(),
                    appointment.getUserName()
            ));
        }
        return appointmentDtoList;
    }

    @Override
    public int getAppointmentCount() throws SQLException, ClassNotFoundException {
        return appointmentDao.getAppointmentCount();
    }

    @Override
    public List<AppointmentDto> getAppointmentsByDoctorAndDate(int doctorId, Date date) throws SQLException, ClassNotFoundException {
        List<AppointmentDto> appointmentDtoList = new ArrayList<>();
        for (Appointment appointment : appointmentDao.getAppointmentsByDoctorAndDate(doctorId, date)) {
            appointmentDtoList.add(new AppointmentDto(
                    appointment.getId(), appointment.getDate(), appointment.getTime(), appointment.getReason(),
                    appointment.getStatus(), appointment.getPatientName(), appointment.getDoctorName(),
                    appointment.getUserName()
            ));
        }
        return appointmentDtoList;
    }
}
