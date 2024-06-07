package com.moha.hms.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AppointmentDto {
    private String id;
    private Date date;
    private Date time;
    private String patientName;
    private String doctorName;
    private String reason;
    private String status;
    private String userName;
}
