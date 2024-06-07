package com.moha.hms.view.tm;

import javafx.scene.control.Button;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DepartmentTm {
    private String count;
    private String id;
    private String name;
    private Integer floor;
    private String managerName;
    private Button updateBtn;
    private Button deleteBtn;
}
