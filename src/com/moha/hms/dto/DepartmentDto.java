package com.moha.hms.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DepartmentDto {
    private String id;
    private String name;
    private Integer floor;
    private String managerName;
}
