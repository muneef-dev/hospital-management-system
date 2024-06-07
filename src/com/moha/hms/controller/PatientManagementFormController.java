package com.moha.hms.controller;

import javafx.event.ActionEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class PatientManagementFormController extends SuperBaseController {
    public BorderPane context;

    public void btnViewAndManageAllOnAction(ActionEvent event) throws IOException {
        setUi("PatientsViewAndManageAllForm",context);
    }

    public void btnAddNewOnAction(ActionEvent event) throws IOException {
        setUi("PatientAddNewForm",context);
    }
}
