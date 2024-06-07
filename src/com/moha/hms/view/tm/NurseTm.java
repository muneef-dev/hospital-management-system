package com.moha.hms.view.tm;

import javafx.scene.control.Button;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class NurseTm {
    private String count;
    private String id;
    private String name;
    private String email;
    private String contactNumber;
    private String departmentName;
    private Button updateBtn;
    private Button deleteBtn;
}
