package com.moha.hms.controller;

import javafx.event.ActionEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class AppointmentManagementFormController extends SuperBaseController {
    public BorderPane context;

    public void btnViewAndManageAllOnAction(ActionEvent event) throws IOException {
        setUi("AppointmentsViewAndManageAllForm",context);
    }

    public void btnAddNewOnAction(ActionEvent event) throws IOException {
        setUi("AppointmentAddNewForm",context);
    }
}
