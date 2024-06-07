package com.moha.hms.entity;

import lombok.*;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Appointment implements SuperEntity{
    private String id;
    private Date date;
    private Date time;
    private String patientName;
    private String doctorName;
    private String reason;
    private String status;
    private String userName;
}
