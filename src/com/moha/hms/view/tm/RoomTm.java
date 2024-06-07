package com.moha.hms.view.tm;

import javafx.scene.control.Button;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RoomTm {
    private String count;
    private String id;
    private String name;
    private Integer bedCount;
    private String availability;
    private String departmentName;
    private Button updateBtn;
    private Button deleteBtn;
}
