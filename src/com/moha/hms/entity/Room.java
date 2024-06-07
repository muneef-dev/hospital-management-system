package com.moha.hms.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Room implements SuperEntity{
    private String id;
    private String name;
    private Integer bedCount;
    private String availability;
    private String departmentName;
}
