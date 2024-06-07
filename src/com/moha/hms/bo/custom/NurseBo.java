package com.moha.hms.bo.custom;

import com.moha.hms.bo.SuperBo;
import com.moha.hms.dto.NurseDto;

import java.sql.SQLException;
import java.util.List;

public interface NurseBo extends SuperBo {
    boolean createNurse(NurseDto nurseDto) throws SQLException, ClassNotFoundException;
    List<NurseDto> searchNurse(String id) throws SQLException, ClassNotFoundException;
    boolean deleteNurse(String id) throws SQLException, ClassNotFoundException;
    boolean updateNurse(NurseDto nurseDto) throws SQLException, ClassNotFoundException;
    List<NurseDto> loadAllNurses() throws SQLException, ClassNotFoundException;
    String getNurseName(String id) throws SQLException, ClassNotFoundException;
}
