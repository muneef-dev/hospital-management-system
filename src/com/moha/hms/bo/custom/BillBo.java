package com.moha.hms.bo.custom;

import com.moha.hms.bo.SuperBo;
import com.moha.hms.dto.BillDto;

import java.sql.SQLException;
import java.util.List;

public interface BillBo extends SuperBo {
    boolean createBill(BillDto billDto) throws SQLException, ClassNotFoundException;
    List<BillDto> searchBill(String id) throws SQLException, ClassNotFoundException;
    boolean deleteBill(String id) throws SQLException, ClassNotFoundException;
    boolean updateBill(BillDto billDto) throws SQLException, ClassNotFoundException;
    List<BillDto> loadAllBills() throws SQLException, ClassNotFoundException;
    double getTotalAmountOfColumns(String columnName) throws SQLException, ClassNotFoundException;
}
