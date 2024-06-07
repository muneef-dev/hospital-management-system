package com.moha.hms.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DoctorDto {
    private String id;
    private String name;
    private String email;
    private String specialistFiled;
    private String contactNumber;
    private String departmentName;
}
