package com.moha.hms.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RoomDto {
    private String id;
    private String name;
    private Integer bedCount;
    private String availability;
    private String departmentName;
}
