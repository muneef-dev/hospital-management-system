package com.moha.hms.view.tm;

import javafx.scene.control.Button;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserRoleTm {
    private String count;
    private String id;
    private String roleDescription;
    private String roleName;
    private Button updateBtn;
    private Button deleteBtn;
}
