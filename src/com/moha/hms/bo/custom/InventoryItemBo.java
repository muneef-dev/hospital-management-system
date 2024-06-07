package com.moha.hms.bo.custom;

import com.moha.hms.bo.SuperBo;
import com.moha.hms.dto.InventoryItemDto;

import java.sql.SQLException;
import java.util.List;

public interface InventoryItemBo extends SuperBo {
    boolean createInventoryItem(InventoryItemDto inventoryItemDto) throws SQLException, ClassNotFoundException;
    List<InventoryItemDto> searchInventoryItem(String id) throws SQLException, ClassNotFoundException;
    boolean deleteInventoryItem(String id) throws SQLException, ClassNotFoundException;
    boolean updateInventoryItem(InventoryItemDto inventoryItemDto) throws SQLException, ClassNotFoundException;
    List<InventoryItemDto> loadAllInventoryItems() throws SQLException, ClassNotFoundException;
    List<InventoryItemDto> getItemsWithLowStock() throws SQLException, ClassNotFoundException; // New method
}
