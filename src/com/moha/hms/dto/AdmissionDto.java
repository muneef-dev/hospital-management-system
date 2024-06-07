package com.moha.hms.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AdmissionDto {
    private String id;
    private Date admittingDate;
    private Date dischargeDate;
    private String patientName;
    private String roomName;
    private String doctorName;
    private String diagnosisId;
}
