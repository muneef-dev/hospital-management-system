package com.moha.hms.dao.custom.impl;

import com.moha.hms.dao.CrudUtil;
import com.moha.hms.dao.custom.NurseDao;
import com.moha.hms.entity.Nurse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NurseDaoImpl implements NurseDao {
    @Override
    public boolean create(Nurse nurse) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO nurse VALUES (?,?,?,?,?)",
                nurse.getId(), nurse.getName(), nurse.getEmail(),nurse.getContactNumber(),nurse.getDepartmentName());
    }

    @Override
    public List<Nurse> search(String id) throws SQLException, ClassNotFoundException {
        id = "%"+id+"%";
        ResultSet resultSet = CrudUtil.execute(
                "SELECT * FROM nurse WHERE property_id LIKE ? || name Like ? || email LIKE ?",id,id,id);
        List<Nurse> nurseList = new ArrayList<>();
        while (resultSet.next()){
            nurseList.add(new Nurse(
                    resultSet.getString(1), resultSet.getString(2),
                    resultSet.getString(3),resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }
        return nurseList;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM nurse WHERE property_id=?",id);
    }

    @Override
    public boolean update(Nurse nurse) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE nurse SET " +
                "name=?,email=?,contact_number=?,department_property_id=? WHERE property_id=?",
                nurse.getName(), nurse.getEmail(),
                nurse.getContactNumber(),nurse.getDepartmentName(),nurse.getId());
    }

    @Override
    public List<Nurse> loadAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM nurse");
        List<Nurse> nurseList = new ArrayList<>();
        while (resultSet.next()){
            nurseList.add(new Nurse(
                    resultSet.getString(1), resultSet.getString(2),
                    resultSet.getString(3),resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }
        return nurseList;
    }

    @Override
    public String getNurseName(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet =  CrudUtil.execute("SELECT name FROM nurse WHERE property_id=?",id);
        String name = null;
        while (resultSet.next()){
            name = resultSet.getString(1);
        }
        return name;
    }
}
