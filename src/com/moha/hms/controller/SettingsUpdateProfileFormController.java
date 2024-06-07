package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.UserRoleBo;
import com.moha.hms.dto.UserDto;
import com.moha.hms.dto.UserRoleDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SettingsUpdateProfileFormController extends SuperBaseController {
    public BorderPane context;
    public TextField txtNewName;

    public void initialize() {
    }

    public void clearBtnOnAction(ActionEvent event) {
        txtNewName.clear();
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("SettingsForm", context);
    }

    public void btnContinueOnAction(ActionEvent event) {
        if (preventNullValueOfSaveUser()) {
            try {
                UserDto userDto = new UserDto(
                        getCurrentUser().getId(), getCurrentUser().getEmail(), txtNewName.getText(),
                        getCurrentUser().getPassword(), getCurrentUser().getRole());

                boolean user = userBo.updateUser(userDto);

                if (user) {
                    new Alert(Alert.AlertType.INFORMATION, "User name is updated and you have to re login").show();
                    clear();
                    setMainUi("LoginForm",context);
                }
            } catch(SQLException | ClassNotFoundException | IOException e){
                new Alert(Alert.AlertType.WARNING,e.getMessage()).show();
            }
        }
    }

    public boolean preventNullValueOfSaveUser() {
        if (txtNewName.getText().isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION, "Fill the all fields").show();
            return false;
        }
        return true;
    }

    public void clear(){
        txtNewName.clear();
    }
}
