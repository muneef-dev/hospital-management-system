package com.moha.hms.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User implements SuperEntity{
    private String id;
    private String email;
    private String name;
    private String password;
    private String role;
}
