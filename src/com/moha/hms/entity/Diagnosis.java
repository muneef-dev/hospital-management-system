package com.moha.hms.entity;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Diagnosis implements SuperEntity {
    private String id;
    private String description;
    private Date date;
    private String patientType;
    private String appointmentDate;
    private String patientName;
    private String doctorName;
    private String nurseName;
}
