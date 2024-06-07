package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.DepartmentBo;
import com.moha.hms.bo.custom.NurseBo;
import com.moha.hms.dto.DepartmentDto;
import com.moha.hms.dto.NurseDto;
import com.moha.hms.util.KeyGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NurseAddNewFormController extends SuperBaseController {
    public BorderPane context;
    public ImageView backBtnImg;
    public ImageView viewBtnImg;
    public ImageView saveBtnImg;
    public ImageView clearBtnImg;
    public TextField txtName;
    public TextField txtContactNo;
    public TextField txtEmail;
    public ComboBox<String> cmbDepartmentName;
    public Button saveBtn;

    NurseBo nurseBo = BoFactory.getInstance().getBo(BoFactory.BoType.NURSE);
    DepartmentBo departmentBo = BoFactory.getInstance().getBo(BoFactory.BoType.DEPARTMENT);
    ObservableList<String> observableListDepartments = FXCollections.observableArrayList();
    List<DepartmentDto> departmentDtoList = new ArrayList<>();

    public void initialize() throws SQLException, ClassNotFoundException {
        loadAllDepartments();
    }

    public void loadAllDepartments() throws SQLException, ClassNotFoundException {
        observableListDepartments.clear();
        departmentDtoList = departmentBo.loadAllDepartments();
        for (DepartmentDto dto: departmentDtoList) {
            observableListDepartments.add(dto.getName());
        }
        cmbDepartmentName.setItems(observableListDepartments);
    }

    public void saveBtnOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String department = cmbDepartmentName.getValue();
        Optional<DepartmentDto> selectedDepartmentDto =
                departmentDtoList.stream().filter(e -> e.getName().equals(department)).findFirst();
        if (preventNullValueOfSaveNurse()) {
            boolean nurse = nurseBo.createNurse(new NurseDto(
                    KeyGenerator.generateId(), txtName.getText(), txtEmail.getText(),
                    txtContactNo.getText(), selectedDepartmentDto.get().getId()
            ));

            if (nurse) {
                new Alert(Alert.AlertType.INFORMATION, "Nurse is Saved").show();
                clear();
            }
        }
    }

    public boolean preventNullValueOfSaveNurse() {
        if (txtName.getText().isEmpty() || txtContactNo.getText().isEmpty() || txtEmail.getText().isEmpty() ||
                cmbDepartmentName.getValue() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Fill all the fields").show();
            return false;
        }

        if (!isValidEmail(txtEmail.getText())) {
            new Alert(Alert.AlertType.WARNING, "Enter a VALID EMAIL ADDRESS (e.g., moha@example.com)").show();
            return false;
        }

        // Check if contact number is numeric
        if (!isNumeric(txtContactNo.getText())) {
            new Alert(Alert.AlertType.WARNING, "Enter NUMERIC VALUES for Contact Number").show();
            return false;
        } else if (!isValidPhoneNumber(txtContactNo.getText())) {
            new Alert(Alert.AlertType.WARNING,
                    "Enter a VALID PHONE NUMBER (You entered less than or greater than 10 digit)").show();
            return false;
        }
        return true;
    }

    public void clearBtnOnAction(ActionEvent event) {
        clear();
    }

    public void clear() {
        txtName.clear();
        txtContactNo.clear();
        txtEmail.clear();
        cmbDepartmentName.setValue(null);
    }

    public void setNurseData(String name, String contactNo, String email,
                             String departmentName) {
        txtName.setText(name);
        txtContactNo.setText(contactNo);
        txtEmail.setText(email);
        cmbDepartmentName.setValue(departmentName);
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("NurseManagementForm", context);
    }

    public void viewBtnOnAction(ActionEvent event) throws IOException {
        setUi("NursesViewAndManageAllForm", context);
    }
}
