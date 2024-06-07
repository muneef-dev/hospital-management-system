package com.moha.hms.bo.custom;

import com.moha.hms.bo.SuperBo;
import com.moha.hms.dto.DoctorDto;

import java.sql.SQLException;
import java.util.List;

public interface DoctorBo extends SuperBo {
    boolean createDoctor(DoctorDto doctorDto) throws SQLException, ClassNotFoundException;
    List<DoctorDto> searchDoctor(String id) throws SQLException, ClassNotFoundException;
    boolean deleteDoctor(String id) throws SQLException, ClassNotFoundException;
    boolean updateDoctor(DoctorDto doctorDto) throws SQLException, ClassNotFoundException;
    List<DoctorDto> loadAllDoctors() throws SQLException, ClassNotFoundException;
    String getDoctorName(String id) throws SQLException, ClassNotFoundException;

}
