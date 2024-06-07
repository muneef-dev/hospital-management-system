package com.moha.hms.view.tm;

import javafx.scene.control.Button;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class InventoryItemTm {
    private String count;
    private String id;
    private String name;
    private String qrCode;
    private String category;
    private Integer qtyOnHand;
    private Integer minimumOrderQuantity;
    private String supplierName;
    private Double buyingPrice;
    private Double sellingPrice;
    private Button updateBtn;
    private Button deleteBtn;
}
