package com.moha.hms.controller;

import javafx.event.ActionEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class SettingsFormController extends SuperBaseController {
    public BorderPane context;

    public void btnUpdateProfileOnAction(ActionEvent event) throws IOException {
        setUi("SettingsUpdateProfileForm", context);
    }

    public void btnChangePasswordOnAction(ActionEvent event) throws IOException {
        setUi("SettingsChangePasswordForm", context);
    }
}
