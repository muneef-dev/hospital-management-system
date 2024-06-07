package com.moha.hms.dto;

import com.moha.hms.entity.SuperEntity;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DiagnosisDto {
    private String id;
    private String description;
    private Date date;
    private String patientType;
    private String appointmentId;
    private String patientName;
    private String doctorName;
    private String nurseName;
}
