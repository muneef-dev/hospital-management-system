package com.moha.hms.controller;

import javafx.event.ActionEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class AdmissionManagementFormController extends SuperBaseController{
    public BorderPane context;

    public void btnViewAndManageAllOnAction(ActionEvent event) throws IOException {
        setUi("AdmissionsViewAndManageAllForm",context);
    }
    public void btnAddNewOnAction(ActionEvent event) throws IOException {
        setUi("AdmissionAddNewForm",context);
    }
}
