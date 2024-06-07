package com.moha.hms.dao.custom.impl;

import com.moha.hms.dao.CrudUtil;
import com.moha.hms.dao.custom.InventoryItemDao;
import com.moha.hms.entity.InventoryItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryItemDaoImpl implements InventoryItemDao {
    @Override
    public boolean create(InventoryItem inventoryItem) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO inventory_item VALUES (?,?,?,?,?,?,?,?,?)",
                inventoryItem.getId(), inventoryItem.getName(), inventoryItem.getQrCode(),inventoryItem.getCategory(),
                inventoryItem.getQtyOnHand(),inventoryItem.getMinimumOrderQuantity(),inventoryItem.getSupplierName(),
                inventoryItem.getBuyingPrice(),inventoryItem.getSellingPrice());
    }

    @Override
    public List<InventoryItem> search(String id) throws SQLException, ClassNotFoundException {
        id = "%"+id+"%";
        ResultSet resultSet = CrudUtil.execute(
                "SELECT * FROM inventory_item WHERE property_id LIKE ? || name Like ? || supplier LIKE ?",id,id,id);
        List<InventoryItem> inventoryItemList = new ArrayList<>();
        while (resultSet.next()){
            inventoryItemList.add(new InventoryItem(
                    resultSet.getString(1),resultSet.getString(2),
                    resultSet.getString(3),resultSet.getString(4),
                    resultSet.getInt(5),resultSet.getInt(6),
                    resultSet.getString(7),resultSet.getDouble(8),
                    resultSet.getDouble(9)
            ));
        }
        return inventoryItemList;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM inventory_item WHERE property_id=?",id);
    }

    @Override
    public boolean update(InventoryItem inventoryItem) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE inventory_item SET " +
                "name=?,qrcode=?,category=?,qty_on_hand=?,minimum_order_qty=?,supplier=?,buying_price=?," +
                        "selling_price=? WHERE property_id=?",
                inventoryItem.getName(), inventoryItem.getQrCode(),inventoryItem.getCategory(),
                inventoryItem.getQtyOnHand(),inventoryItem.getMinimumOrderQuantity(),inventoryItem.getSupplierName(),
                inventoryItem.getBuyingPrice(),inventoryItem.getSellingPrice(),inventoryItem.getId());
    }

    @Override
    public List<InventoryItem> loadAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM inventory_item");
        List<InventoryItem> inventoryItemList = new ArrayList<>();
        while (resultSet.next()){
            inventoryItemList.add(new InventoryItem(
                    resultSet.getString(1),resultSet.getString(2),
                    resultSet.getString(3),resultSet.getString(4),
                    resultSet.getInt(5),resultSet.getInt(6),
                    resultSet.getString(7),resultSet.getDouble(8),
                    resultSet.getDouble(9)
            ));
        }
        return inventoryItemList;
    }

    @Override
    public List<InventoryItem> getItemsWithLowStock() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute(
                "SELECT name, qty_on_hand FROM inventory_item WHERE qty_on_hand <= minimum_order_qty");
        List<InventoryItem> inventoryItemList = new ArrayList<>();
        while (resultSet.next()) {
            inventoryItemList.add(new InventoryItem(
                    null, resultSet.getString(1),
                    null, null,
                    resultSet.getInt(2), 0,
                    null, 0.0,
                    0.0
            ));
        }
        return inventoryItemList;
    }

//    @Override
//    public List<InventoryItem> inventoryPatient(String patientId) throws SQLException, ClassNotFoundException {
//        String query = "SELECT property_id, name, qrcode, category, qty_on_hand, " +
//                "patient_property_id, doctor_property_id, nurse_property_id " +
//                "FROM diagnosis WHERE patient_property_id=?";
//        ResultSet resultSet = CrudUtil.execute(query, patientId);
//        List<InventoryItem> inventoryItemList = new ArrayList<>();
//        while (resultSet.next()) {
//            inventoryItemList.add(new InventoryItem(
//                    resultSet.getString(1),
//                    resultSet.getString(2),
//                    resultSet.getString(3),
//                    resultSet.getString(4),
//                    resultSet.getInt(5),
//                    resultSet.getInt(6),
//                    resultSet.getString(7),
//                    resultSet.getDouble(8),
//                    resultSet.getDouble(9)
//            ));
//        }
//        return inventoryItemList;
//    }
}
