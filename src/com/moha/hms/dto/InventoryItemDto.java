package com.moha.hms.dto;

import com.moha.hms.entity.SuperEntity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class InventoryItemDto {
    private String id;
    private String name;
    private String qrCode;
    private String category;
    private Integer qtyOnHand;
    private Integer minimumOrderQuantity;
    private String supplierName;
    private Double buyingPrice;
    private Double sellingPrice;
}
