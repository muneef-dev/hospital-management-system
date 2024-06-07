package com.moha.hms.controller;

import javafx.event.ActionEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class BillManagementFormController extends SuperBaseController {
    public BorderPane context;

    public void btnViewAndManageAllOnAction(ActionEvent event) throws IOException {
        setUi("BillsViewAndManageAllForm",context);
    }

    public void btnAddNewOnAction(ActionEvent event) throws IOException {
        setUi("BillAddNewForm",context);

    }
}