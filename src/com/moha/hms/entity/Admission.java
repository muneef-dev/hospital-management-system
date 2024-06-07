package com.moha.hms.entity;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Admission implements SuperEntity {
    private String id;
    private Date admittingDate;
    private Date dischargeDate;
    private String patientName;
    private String roomName;
    private String doctorName;
    private String diagnosisId;
}
