package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.UserRoleBo;
import com.moha.hms.dto.UserRoleDto;
import com.moha.hms.util.KeyGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRoleAddNewFormController extends SuperBaseController {
    public BorderPane context;
    public TextField txtRoleName;
    public ComboBox txtRoles;
    public Button saveBtn;
    public TextField txtRoleDescription;

    UserRoleBo userRoleBo = BoFactory.getInstance().getBo(BoFactory.BoType.USER_ROLE);
    ObservableList<String> observableListRoles = FXCollections.observableArrayList();
    List<UserRoleDto> userRoleDtos = new ArrayList<>();

    public void initialize() throws SQLException, ClassNotFoundException {
        /*loadAllRoles();*/
    }

    /*private void loadAllRoles() throws SQLException, ClassNotFoundException {
        observableListRoles.clear();
        userRoleDtos = userRoleBo.loadAllUserRoles();
        for (UserRoleDto dto: userRoleDtos
             ) {
            observableListRoles.add(dto.getRoleName());
        }
        txtRoles.setItems(observableListRoles);
    }*/

    public void clearBtnOnAction(ActionEvent event) {
        clear();
    }

    public void saveBtnOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (preventNullValueOfSaveUserRole()) {
            if (!userRoleBo.isRoleExist(txtRoleName.getText().toUpperCase())) {
                boolean user = userRoleBo.createUserRole(new UserRoleDto(
                        KeyGenerator.generateId(),txtRoleDescription.getText(),
                        txtRoleName.getText().toUpperCase()
                ));

                if (user){
                    new Alert(Alert.AlertType.INFORMATION,"User Role is Saved").show();
                    clear();
                }
            }else {
                new Alert(Alert.AlertType.WARNING,"User Role is already exist").show();
            }
        }
    }

    public boolean preventNullValueOfSaveUserRole() {
        if (txtRoleName.getText().isEmpty() || txtRoleDescription.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Fill the all fields").show();
            return false;
        }
        return true;
    }

    public void clear(){
        txtRoleName.clear();
        txtRoleDescription.clear();
    }

    public void setUserRoleData(String description, String roleName) {
        txtRoleDescription.setText(description);
        txtRoleName.setText(roleName);
    }
    public void viewBtnOnAction(ActionEvent event) throws IOException {
        setUi("UserRolesViewAndManageAllForm",context);
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("UserManagementForm",context);
    }
}
