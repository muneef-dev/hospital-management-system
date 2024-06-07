package com.moha.hms.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserRoleDto {
    private String id;
    private String roleDescription;
    private String roleName;
}
