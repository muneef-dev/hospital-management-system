package com.moha.hms.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Nurse implements SuperEntity{
    private String id;
    private String name;
    private String email;
    private String contactNumber;
    private String departmentName;
}
