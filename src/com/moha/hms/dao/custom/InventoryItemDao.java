package com.moha.hms.dao.custom;

import com.moha.hms.dao.CrudDao;
import com.moha.hms.entity.Admission;
import com.moha.hms.entity.InventoryItem;

import java.sql.SQLException;
import java.util.List;

public interface InventoryItemDao extends CrudDao<InventoryItem,String> {
    List<InventoryItem> getItemsWithLowStock() throws SQLException, ClassNotFoundException; // New method
}
