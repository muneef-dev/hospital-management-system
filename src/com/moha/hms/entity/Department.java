package com.moha.hms.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Department implements SuperEntity{
    private String id;
    private String name;
    private Integer floor;
    private String managerName;
}
