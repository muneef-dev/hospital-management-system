package com.moha.hms.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class InventoryItem implements SuperEntity {
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
