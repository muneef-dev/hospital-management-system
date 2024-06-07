package com.moha.hms.entity;

import lombok.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Bill implements SuperEntity{
    private String id;
    private String qrCode;
    private Date issueDate;
    private String paymentStatus;
    private Double doctorFee;
    private Double medicationCost;
    private Double labTestCost;
    private Double otherServicesCost;
    private String patientName;
    private String medicationId;
    private String userId;
}
