package com.moha.hms.bo.custom.impl;

import com.moha.hms.bo.custom.DoctorBo;
import com.moha.hms.dao.DaoFactory;
import com.moha.hms.dao.custom.DoctorDao;
import com.moha.hms.dto.DoctorDto;
import com.moha.hms.entity.Doctor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorBoImpl implements DoctorBo {

    DoctorDao doctorDao = DaoFactory.getInstance().getDao(DaoFactory.DaoType.DOCTOR);

    @Override
    public boolean createDoctor(DoctorDto doctorDto) throws SQLException, ClassNotFoundException {
        return doctorDao.create(new Doctor(
                doctorDto.getId(),doctorDto.getName(),doctorDto.getEmail(),
                doctorDto.getSpecialistFiled(),doctorDto.getContactNumber(), doctorDto.getDepartmentName()
        ));
    }

    @Override
    public List<DoctorDto> searchDoctor(String id) throws SQLException, ClassNotFoundException {
        List<DoctorDto> doctorDtoList = new ArrayList<>();
        for (Doctor doctor: doctorDao.search(id)
             ) {
            doctorDtoList.add(new DoctorDto(
                    doctor.getId(),doctor.getName(),doctor.getEmail(),
                    doctor.getSpecialistFiled(),doctor.getContactNumber(), doctor.getDepartmentName()
            ));
        }
        return doctorDtoList;
    }

    @Override
    public boolean deleteDoctor(String id) throws SQLException, ClassNotFoundException {
        return doctorDao.delete(id);
    }

    @Override
    public boolean updateDoctor(DoctorDto doctorDto) throws SQLException, ClassNotFoundException {
        return doctorDao.update(new Doctor(
                doctorDto.getId(),doctorDto.getName(),doctorDto.getEmail(),
                doctorDto.getSpecialistFiled(),doctorDto.getContactNumber(), doctorDto.getDepartmentName()
        ));
    }

    @Override
    public List<DoctorDto> loadAllDoctors() throws SQLException, ClassNotFoundException {
        List<DoctorDto> doctorDtoList = new ArrayList<>();
        for (Doctor doctor : doctorDao.loadAll()
             ) {
            doctorDtoList.add(new DoctorDto(
                    doctor.getId(),doctor.getName(),doctor.getEmail(),
                    doctor.getSpecialistFiled(),doctor.getContactNumber(), doctor.getDepartmentName()
            ));
        }
        return doctorDtoList;
    }

    @Override
    public String getDoctorName(String id) throws SQLException, ClassNotFoundException {
        return doctorDao.getDoctorName(id);
    }
}
