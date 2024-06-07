package com.moha.hms.view.tm;

import javafx.scene.control.Button;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AdmissionTm {
    private String count;
    private String id;
    private Date admittingDate;
    private Date dischargeDate;
    private String patientName;
    private String roomName;
    private String doctorName;
    private String diagnosisId;
    private Button updateBtn;
    private Button deleteBtn;
}
