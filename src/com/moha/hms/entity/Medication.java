package com.moha.hms.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Medication implements SuperEntity{
    private String id;
    private String patientId;
    private String patientName;
    private Integer soldQty;
    private Double totalCost;
    private String diagnosisId;
    private String inventoryItemId;
}
