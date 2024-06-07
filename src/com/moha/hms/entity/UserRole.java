package com.moha.hms.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserRole implements SuperEntity{
    private String id;
    private String roleDescription;
    private String roleName;
}
