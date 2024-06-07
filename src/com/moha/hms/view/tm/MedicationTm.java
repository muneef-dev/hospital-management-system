package com.moha.hms.view.tm;

import javafx.scene.control.Button;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MedicationTm {
    private String count;
    private String id;
    private String patientId;
    private String patientName;
    private Integer soldQty;
    private Double totalCost;
    private String diagnosisId;
    private String inventoryItemId;
    private Button updateBtn;
    private Button deleteBtn;
}
