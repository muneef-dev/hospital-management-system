package com.moha.hms.view.tm;

import javafx.scene.control.Button;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DiagnosisTm {
    private String count;
    private String id;
    private String description;
    private Date date;
    private String patientType;
    private String appointmentDate;
    private String patientName;
    private String doctorName;
    private String nurseName;
    private Button updateBtn;
    private Button deleteBtn;
}
