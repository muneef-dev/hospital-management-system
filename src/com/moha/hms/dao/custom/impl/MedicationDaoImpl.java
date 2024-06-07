package com.moha.hms.dao.custom.impl;

import com.moha.hms.dao.CrudUtil;
import com.moha.hms.dao.custom.MedicationDao;
import com.moha.hms.entity.Medication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicationDaoImpl implements MedicationDao {
    @Override
    public boolean create(Medication medication) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO medication VALUES (?,?,?,?,?,?,?)",
                medication.getId(), medication.getPatientId(), medication.getPatientName(),
                medication.getSoldQty(), medication.getTotalCost(),
                medication.getDiagnosisId(), medication.getInventoryItemId());
    }

    @Override
    public List<Medication> search(String id) throws SQLException, ClassNotFoundException {
        id = "%" + id + "%";
        ResultSet resultSet = CrudUtil.execute(
                "SELECT * FROM medication WHERE property_id LIKE ? OR patient_name LIKE ? OR diagnosis_property_id LIKE ? OR inventory_item_property_id LIKE ?",
                id, id, id, id);
        List<Medication> medicationList = new ArrayList<>();
        while (resultSet.next()) {
            medicationList.add(new Medication(
                    resultSet.getString("property_id"),
                    resultSet.getString("patient_property_id"),
                    resultSet.getString("patient_name"),
                    resultSet.getInt("sold_qty"),
                    resultSet.getDouble("total_cost"),
                    resultSet.getString("diagnosis_property_id"),
                    resultSet.getString("inventory_item_property_id")
            ));
        }
        return medicationList;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM medication WHERE property_id=?", id);
    }

    @Override
    public boolean update(Medication medication) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE medication SET " +
                        "sold_qty=?, total_cost=?, diagnosis_property_id=?, inventory_item_property_id=?, patient_property_id=?, patient_name=? WHERE property_id=?",
                medication.getSoldQty(), medication.getTotalCost(), medication.getDiagnosisId(),
                medication.getInventoryItemId(), medication.getPatientId(), medication.getPatientName(), medication.getId());
    }

    @Override
    public List<Medication> loadAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM medication");
        List<Medication> medicationList = new ArrayList<>();
        while (resultSet.next()) {
            medicationList.add(new Medication(
                    resultSet.getString("property_id"),
                    resultSet.getString("patient_property_id"),
                    resultSet.getString("patient_name"),
                    resultSet.getInt("sold_qty"),
                    resultSet.getDouble("total_cost"),
                    resultSet.getString("diagnosis_property_id"),
                    resultSet.getString("inventory_item_property_id")
            ));
        }
        return medicationList;
    }

    @Override
    public List<Medication> isMedicationAvailableForPatient(String diagnosisId) throws SQLException, ClassNotFoundException {
        String query = "SELECT property_id, patient_property_id, patient_name, sold_qty, total_cost, diagnosis_property_id," +
                " inventory_item_property_id FROM medication WHERE diagnosis_property_id=?";

        ResultSet resultSet = CrudUtil.execute(query, diagnosisId);
        List<Medication> medicationList = new ArrayList<>();

        while (resultSet.next()) {
            medicationList.add(new Medication(
                    resultSet.getString("property_id"),
                    resultSet.getString("patient_property_id"),
                    resultSet.getString("patient_name"),
                    resultSet.getInt("sold_qty"),
                    resultSet.getDouble("total_cost"),
                    resultSet.getString("diagnosis_property_id"),
                    resultSet.getString("inventory_item_property_id")
            ));
        }
        return medicationList;
    }
}
