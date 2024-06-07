package com.moha.hms.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MedicationManagementFormController extends SuperBaseController {
    public BorderPane context;
    public Button btnViewAndManageAll;
    public Button btnAddNew;

    public void btnViewAndManageAllOnAction(ActionEvent event) throws IOException {
        setUi("MedicationsViewAndManageAllForm",context);

    }

    public void btnAddNewOnAction(ActionEvent event) throws IOException {
        setUi("MedicationAddNewForm",context);

    }
}
