package com.moha.hms.view.tm;

import javafx.scene.control.Button;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BillTm {
    private String count;
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
    private Button updateBtn;
    private Button deleteBtn;
}
