package com.moha.hms.bo.custom.impl;

import com.moha.hms.bo.custom.NurseBo;
import com.moha.hms.dao.DaoFactory;
import com.moha.hms.dao.custom.NurseDao;
import com.moha.hms.dto.NurseDto;
import com.moha.hms.entity.Nurse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NurseBoImpl implements NurseBo {

    NurseDao nurseDao = DaoFactory.getInstance().getDao(DaoFactory.DaoType.NURSE);

    @Override
    public boolean createNurse(NurseDto nurseDto) throws SQLException, ClassNotFoundException {
        return nurseDao.create(new Nurse(
                nurseDto.getId(),nurseDto.getName(),nurseDto.getEmail(),
                nurseDto.getContactNumber(), nurseDto.getDepartmentName()
        ));
    }

    @Override
    public List<NurseDto> searchNurse(String id) throws SQLException, ClassNotFoundException {
        List<NurseDto> nurseDtoList = new ArrayList<>();
        for (Nurse nurse: nurseDao.search(id)
             ) {
            nurseDtoList.add(new NurseDto(
                    nurse.getId(),nurse.getName(),nurse.getEmail(),
                    nurse.getContactNumber(), nurse.getDepartmentName()
            ));
        }
        return nurseDtoList;
    }

    @Override
    public boolean deleteNurse(String id) throws SQLException, ClassNotFoundException {
        return nurseDao.delete(id);
    }

    @Override
    public boolean updateNurse(NurseDto nurseDto) throws SQLException, ClassNotFoundException {
        return nurseDao.update(new Nurse(
                nurseDto.getId(),nurseDto.getName(),nurseDto.getEmail(),
                nurseDto.getContactNumber(), nurseDto.getDepartmentName()
        ));
    }

    @Override
    public List<NurseDto> loadAllNurses() throws SQLException, ClassNotFoundException {
        List<NurseDto> nurseDtoList = new ArrayList<>();
        for (Nurse nurse : nurseDao.loadAll()
             ) {
            nurseDtoList.add(new NurseDto(
                    nurse.getId(),nurse.getName(),nurse.getEmail(),
                    nurse.getContactNumber(), nurse.getDepartmentName()
            ));
        }
        return nurseDtoList;
    }

    @Override
    public String getNurseName(String id) throws SQLException, ClassNotFoundException {
        return nurseDao.getNurseName(id);
    }
}
