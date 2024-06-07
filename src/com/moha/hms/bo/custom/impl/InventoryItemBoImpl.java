package com.moha.hms.bo.custom.impl;

import com.moha.hms.bo.custom.InventoryItemBo;
import com.moha.hms.dao.DaoFactory;
import com.moha.hms.dao.custom.InventoryItemDao;
import com.moha.hms.dto.InventoryItemDto;
import com.moha.hms.entity.InventoryItem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryItemBoImpl implements InventoryItemBo {

    InventoryItemDao inventoryItemDao = DaoFactory.getInstance().getDao(DaoFactory.DaoType.INVENTORY_ITEM);

    @Override
    public boolean createInventoryItem(InventoryItemDto inventoryItemDto) throws SQLException, ClassNotFoundException {
        return inventoryItemDao.create(new InventoryItem(
                inventoryItemDto.getId(), inventoryItemDto.getName(), inventoryItemDto.getQrCode(),inventoryItemDto.getCategory(),
                inventoryItemDto.getQtyOnHand(),inventoryItemDto.getMinimumOrderQuantity(),inventoryItemDto.getSupplierName(),
                inventoryItemDto.getBuyingPrice(),inventoryItemDto.getSellingPrice()
        ));
    }

    @Override
    public List<InventoryItemDto> searchInventoryItem(String id) throws SQLException, ClassNotFoundException {
        List<InventoryItemDto> inventoryItemDtoList = new ArrayList<>();
        for (InventoryItem inventoryItem: inventoryItemDao.search(id)
             ) {
            inventoryItemDtoList.add(new InventoryItemDto(
                    inventoryItem.getId(), inventoryItem.getName(), inventoryItem.getQrCode(),inventoryItem.getCategory(),
                    inventoryItem.getQtyOnHand(),inventoryItem.getMinimumOrderQuantity(),inventoryItem.getSupplierName(),
                    inventoryItem.getBuyingPrice(),inventoryItem.getSellingPrice()
            ));
        }
        return inventoryItemDtoList;
    }

    @Override
    public boolean deleteInventoryItem(String id) throws SQLException, ClassNotFoundException {
        return inventoryItemDao.delete(id);
    }

    @Override
    public boolean updateInventoryItem(InventoryItemDto inventoryItemDto) throws SQLException, ClassNotFoundException {
        return inventoryItemDao.update(new InventoryItem(
                inventoryItemDto.getId(), inventoryItemDto.getName(), inventoryItemDto.getQrCode(),inventoryItemDto.getCategory(),
                inventoryItemDto.getQtyOnHand(),inventoryItemDto.getMinimumOrderQuantity(),inventoryItemDto.getSupplierName(),
                inventoryItemDto.getBuyingPrice(),inventoryItemDto.getSellingPrice()
        ));
    }

    @Override
    public List<InventoryItemDto> loadAllInventoryItems() throws SQLException, ClassNotFoundException {
        List<InventoryItemDto> inventoryItemDtoList = new ArrayList<>();
        for (InventoryItem inventoryItem : inventoryItemDao.loadAll()
             ) {
            inventoryItemDtoList.add(new InventoryItemDto(
                    inventoryItem.getId(), inventoryItem.getName(), inventoryItem.getQrCode(),inventoryItem.getCategory(),
                    inventoryItem.getQtyOnHand(),inventoryItem.getMinimumOrderQuantity(),inventoryItem.getSupplierName(),
                    inventoryItem.getBuyingPrice(),inventoryItem.getSellingPrice()
            ));
        }
        return inventoryItemDtoList;
    }

    @Override
    public List<InventoryItemDto> getItemsWithLowStock() throws SQLException, ClassNotFoundException {
        List<InventoryItemDto> inventoryItemDtoList = new ArrayList<>();
        for (InventoryItem inventoryItem : inventoryItemDao.getItemsWithLowStock()) {
            inventoryItemDtoList.add(new InventoryItemDto(
                    inventoryItem.getId(), inventoryItem.getName(), inventoryItem.getQrCode(), inventoryItem.getCategory(),
                    inventoryItem.getQtyOnHand(), inventoryItem.getMinimumOrderQuantity(), inventoryItem.getSupplierName(),
                    inventoryItem.getBuyingPrice(), inventoryItem.getSellingPrice()
            ));
        }
        return inventoryItemDtoList;
    }
}
