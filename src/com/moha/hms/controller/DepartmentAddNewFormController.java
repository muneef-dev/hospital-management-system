package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.DepartmentBo;
import com.moha.hms.bo.custom.UserBo;
import com.moha.hms.bo.custom.UserRoleBo;
import com.moha.hms.dto.DepartmentDto;
import com.moha.hms.dto.UserDto;
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

public class DepartmentAddNewFormController extends SuperBaseController {
    public BorderPane context;
    public TextField txtName;
    public TextField txtFloor;
    public ComboBox cmbManager;
    public Button saveBtn;

    DepartmentBo departmentBo = BoFactory.getInstance().getBo(BoFactory.BoType.DEPARTMENT);
    UserBo userBo = BoFactory.getInstance().getBo(BoFactory.BoType.USER);
    UserRoleBo userRoleBo = BoFactory.getInstance().getBo(BoFactory.BoType.USER_ROLE);
    ObservableList<String> observableListManager = FXCollections.observableArrayList();

    public void initialize() {
        try {
            loadAllManagers();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadAllManagers() throws SQLException, ClassNotFoundException {
        for (UserDto userDto : userBo.loadAllManagers(userRoleBo.getIdForLoadManagers())) {
            observableListManager.add(userDto.getName());
        }
        cmbManager.setItems(observableListManager);
        cmbManager.setValue(observableListManager.get(0));
    }

    public void saveBtnOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (preventNullValueOfSaveDepartment()) {
            boolean department = departmentBo.createDepartment(new DepartmentDto(
                    setDepartmentId(),txtName.getText(),
                    Integer.parseInt(txtFloor.getText()), String.valueOf(cmbManager.getValue())
            ));

            if (department){
                new Alert(Alert.AlertType.INFORMATION,"Department is Saved").show();
                clear();
            }
        }
    }

    public String setDepartmentId() throws SQLException, ClassNotFoundException {
        String lastDepartmentId = departmentBo.getLastDepartmentId();
        System.out.println(lastDepartmentId);
        if (lastDepartmentId!=null){
            String[] splitData = lastDepartmentId.split("-");
            String lastIntegerNumberAsString = splitData[1];
            int lastIntegerIdAsInt = Integer.parseInt(lastIntegerNumberAsString);
            lastIntegerIdAsInt++;
            return "D-"+lastIntegerIdAsInt;
        }else {
            return "D-1";
        }
    }

    public boolean preventNullValueOfSaveDepartment() {
        if (txtName.getText().isEmpty() || txtFloor.getText().isEmpty() || cmbManager.getValue() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Fill the all fields").show();
            return false;
        }
        if (!isNumeric(txtFloor.getText())) {
            new Alert(
                    Alert.AlertType.WARNING,
                    "Enter NUMERIC VALUES for Floor").show();
            return false;
        }
        return true;
    }

    public void setDepartmentData(String name, String manager, String floor) {
        txtName.setText(name);
        cmbManager.setValue(manager);
        txtFloor.setText(floor);
    }

    public void clear(){
        txtName.clear();
        txtFloor.clear();
        cmbManager.getItems().clear();
    }

    public void clearBtnOnAction(ActionEvent event) {
        clear();
    }

    public void viewBtnOnAction(ActionEvent event) throws IOException {
        setUi("DepartmentViewAndManageAllForm",context);
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("DepartmentManagementForm",context);
    }
}
