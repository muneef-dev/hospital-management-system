package com.moha.hms.bo.custom.impl;

import com.moha.hms.bo.custom.BillBo;
import com.moha.hms.dao.DaoFactory;
import com.moha.hms.dao.custom.BillDao;
import com.moha.hms.dto.BillDto;
import com.moha.hms.entity.Bill;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillBoImpl implements BillBo {

    BillDao billDao = DaoFactory.getInstance().getDao(DaoFactory.DaoType.BILL);

    @Override
    public boolean createBill(BillDto billDto) throws SQLException, ClassNotFoundException {
        return billDao.create(new Bill(
                billDto.getId(), billDto.getQrCode(), billDto.getIssueDate(),billDto.getPaymentStatus(),
                billDto.getDoctorFee(),billDto.getMedicationCost(),billDto.getLabTestCost(),billDto.getOtherServicesCost(),
                billDto.getPatientName(),billDto.getMedicationId(),billDto.getUserId()
        ));
    }

    @Override
    public List<BillDto> searchBill(String id) throws SQLException, ClassNotFoundException {
        List<BillDto> billDtoList = new ArrayList<>();
        for (Bill bill: billDao.search(id)
             ) {
            billDtoList.add(new BillDto(
                    bill.getId(), bill.getQrCode(), bill.getIssueDate(),bill.getPaymentStatus(),
                    bill.getDoctorFee(),bill.getMedicationCost(),bill.getLabTestCost(),bill.getOtherServicesCost(),
                    bill.getPatientName(),bill.getMedicationId(),bill.getUserId()
            ));
        }
        return billDtoList;
    }

    @Override
    public boolean deleteBill(String id) throws SQLException, ClassNotFoundException {
        return billDao.delete(id);
    }

    @Override
    public boolean updateBill(BillDto billDto) throws SQLException, ClassNotFoundException {
        return billDao.update(new Bill(
                billDto.getId(), billDto.getQrCode(), billDto.getIssueDate(),billDto.getPaymentStatus(),
                billDto.getDoctorFee(),billDto.getMedicationCost(),billDto.getLabTestCost(),billDto.getOtherServicesCost(),
                billDto.getPatientName(),billDto.getMedicationId(),billDto.getUserId()
        ));
    }

    @Override
    public List<BillDto> loadAllBills() throws SQLException, ClassNotFoundException {
        List<BillDto> billDtoList = new ArrayList<>();
        for (Bill bill : billDao.loadAll()
             ) {
            billDtoList.add(new BillDto(
                    bill.getId(), bill.getQrCode(), bill.getIssueDate(),bill.getPaymentStatus(),
                    bill.getDoctorFee(),bill.getMedicationCost(),bill.getLabTestCost(),bill.getOtherServicesCost(),
                    bill.getPatientName(),bill.getMedicationId(),bill.getUserId()
            ));
        }
        return billDtoList;
    }

    @Override
    public double getTotalAmountOfColumns(String columnName) throws SQLException, ClassNotFoundException {
        return billDao.getTotalAmountOfColumns(columnName);
    }

}
