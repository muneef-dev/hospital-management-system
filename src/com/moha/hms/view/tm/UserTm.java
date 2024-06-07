package com.moha.hms.view.tm;

import javafx.scene.control.Button;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserTm {
    private String count;
    private String id;
    private String email;
    private String name;
    private String password;
    private String role;
    private Button updateBtn;
    private Button deleteBtn;
}
