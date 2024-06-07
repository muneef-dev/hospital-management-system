package com.moha.hms.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MedicationDto {
    private String id;
    private String patientId;
    private String patientName;
    private Integer soldQty;
    private Double totalCost;
    private String diagnosisId;
    private String inventoryItemId;
}
