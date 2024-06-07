package com.moha.hms.dao.custom.impl;

import com.moha.hms.dao.CrudUtil;
import com.moha.hms.dao.custom.BillDao;
import com.moha.hms.entity.Bill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillDaoImpl implements BillDao {
    @Override
    public boolean create(Bill bill) throws SQLException, ClassNotFoundException {
        System.out.println(bill);
        return CrudUtil.execute("INSERT INTO bill VALUES (?,?,?,?,?,?,?,?,?,?,?)",
                bill.getId(), bill.getQrCode(), bill.getIssueDate(),bill.getPaymentStatus(),
                bill.getDoctorFee(),bill.getMedicationCost(),bill.getLabTestCost(),bill.getOtherServicesCost(),
                bill.getPatientName(),bill.getMedicationId(),bill.getUserId());
    }

    @Override
    public List<Bill> search(String id) throws SQLException, ClassNotFoundException {
        id = "%"+id+"%";
        ResultSet resultSet = CrudUtil.execute(
                "SELECT * FROM bill WHERE property_id LIKE ? || patient_property_id Like ? || user_property_id LIKE ?",id,id,id);
        List<Bill> billList = new ArrayList<>();
        while (resultSet.next()){
            billList.add(new Bill(
                    resultSet.getString(1),resultSet.getString(2),
                    resultSet.getDate(3),resultSet.getString(4),
                    resultSet.getDouble(5),resultSet.getDouble(6),
                    resultSet.getDouble(7),resultSet.getDouble(8),
                    resultSet.getString(9),resultSet.getString(10),
                    resultSet.getString(11)
            ));
        }
        return billList;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM bill WHERE property_id=?",id);
    }

    @Override
    public boolean update(Bill bill) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE bill SET " +
                "qrcode=?,issued_date=?,payment_status=?,doctor_fee=?,medication_cost=?,labtest_cost=?,other_services_cost=?" +
                        ",patient_property_id=?,medication_property_id=?,user_property_id=? WHERE property_id=?",
                bill.getQrCode(), bill.getIssueDate(),bill.getPaymentStatus(),
                bill.getDoctorFee(),bill.getMedicationCost(),bill.getLabTestCost(),bill.getOtherServicesCost(),
                bill.getPatientName(),bill.getMedicationId(),bill.getUserId(),bill.getId());
    }

    @Override
    public List<Bill> loadAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM bill");
        List<Bill> billList = new ArrayList<>();
        while (resultSet.next()){
            billList.add(new Bill(
                    resultSet.getString(1),resultSet.getString(2),
                    resultSet.getDate(3),resultSet.getString(4),
                    resultSet.getDouble(5),resultSet.getDouble(6),
                    resultSet.getDouble(7),resultSet.getDouble(8),
                    resultSet.getString(9),resultSet.getString(10),
                    resultSet.getString(11)
            ));
        }
        return billList;
    }


    // Method to get the total amount of specified columns
    @Override
    public double getTotalAmountOfColumns(String columnName) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(" + columnName + ") FROM bill");
        if (resultSet.next()) {
            return resultSet.getDouble(1);
        }
        return 0; // Return 0 if no result found
    }
}
