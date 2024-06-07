package com.moha.hms.controller;

import javafx.event.ActionEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class UserManagementFormController extends SuperBaseController {
    public BorderPane context;

    public void btnViewAndManageAllOnAction(ActionEvent event) throws IOException {
        setUi("UsersViewAndManageAllForm",context);
    }

    public void btnAddNewOnAction(ActionEvent event) throws IOException {
        setUi("UserAddNewForm",context);
    }

    public void btnUserRolesViewAndManageAllOnAction(ActionEvent event) throws IOException {
        setUi("UserRolesViewAndManageAllForm",context);
    }

    public void btnUserRoleAddNewOnAction(ActionEvent event) throws IOException {
        setUi("UserRoleAddNewForm",context);
    }
}
