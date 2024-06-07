package com.moha.hms.entity;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Patient implements SuperEntity {
    private String id;
    private String nic;
    private String qrCode;
    private String name;
    private Date dob;
    private String age;
    private String gender;
    private String address;
    private String email;
    private String contactNo;
    private String emergencyContactNo;
}
