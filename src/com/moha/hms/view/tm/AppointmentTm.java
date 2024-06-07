package com.moha.hms.view.tm;

import javafx.scene.control.Button;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AppointmentTm {
    private String count;
    private String id;
    private Date date;
    private Date time;
    private String patientName;
    private String doctorName;
    private String reason;
    private String status;
    private String userName;
    private Button updateBtn;
    private Button deleteBtn;
}
