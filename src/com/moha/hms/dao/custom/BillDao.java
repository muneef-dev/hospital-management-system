package com.moha.hms.dao.custom;

import com.moha.hms.dao.CrudDao;
import com.moha.hms.entity.Appointment;
import com.moha.hms.entity.Bill;

import java.sql.SQLException;

public interface BillDao extends CrudDao<Bill,String> {
    double getTotalAmountOfColumns(String columnName) throws SQLException, ClassNotFoundException;
}
