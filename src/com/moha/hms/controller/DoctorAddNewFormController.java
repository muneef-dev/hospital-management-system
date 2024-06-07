package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.DepartmentBo;
import com.moha.hms.bo.custom.DoctorBo;
import com.moha.hms.dto.DepartmentDto;
import com.moha.hms.dto.DoctorDto;
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

public class DoctorAddNewFormController extends SuperBaseController {
    public BorderPane context;
    public ImageView backBtnImg;
    public ImageView viewBtnImg;
    public ImageView saveBtnImg;
    public ImageView clearBtnImg;
    public TextField txtName;
    public TextField txtContactNo;
    public ComboBox<String> cmbDepartmentName;
    public TextField txtEmail;
    public ComboBox<String> cmbSpecialist;
    public Button saveBtn;

    DoctorBo doctorBo = BoFactory.getInstance().getBo(BoFactory.BoType.DOCTOR);
    DepartmentBo departmentBo = BoFactory.getInstance().getBo(BoFactory.BoType.DEPARTMENT);
    ObservableList<String> observableListDepartments = FXCollections.observableArrayList();
    ObservableList<String> observableListSpecialists = FXCollections.observableArrayList();
    List<DepartmentDto> departmentDtoList = new ArrayList<>();

    public void initialize() throws SQLException, ClassNotFoundException {
        loadAllDepartments();
        loadAllSpecialists();
    }

    public void loadAllDepartments() throws SQLException, ClassNotFoundException {
        observableListDepartments.clear();
        departmentDtoList = departmentBo.loadAllDepartments();
        for (DepartmentDto dto: departmentDtoList
        ) {
            observableListDepartments.add(dto.getName());
        }
        cmbDepartmentName.setItems(observableListDepartments);
        //cmbDepartmentName.setValue(observableListDepartments.get(0));
    }

    public void loadAllSpecialists() {
        observableListSpecialists.clear();
        observableListSpecialists.addAll("Cardiologist", "Dermatologist", "Gastroenterologist",
                "Orthopedic", "Pediatrician", "Psychiatrist", "Neurologist", "Oncologist", "General Practitioner", "Other");
        cmbSpecialist.setItems(observableListSpecialists);
    }

    public void saveBtnOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {

        String department = (String) cmbDepartmentName.getValue();
        Optional<DepartmentDto> selectedDepartmentDto =
                departmentDtoList.stream().filter(e -> e.getName().equals(department)).findFirst();
        if (preventNullValueOfSaveDoctor()) {
            boolean doctor = doctorBo.createDoctor(new DoctorDto(
                    KeyGenerator.generateId(), txtName.getText(), txtEmail.getText(),
                    cmbSpecialist.getValue() , txtContactNo.getText(), selectedDepartmentDto.get().getId()
            ));

            if (doctor) {
                new Alert(Alert.AlertType.INFORMATION, "Doctor is Saved").show();
                clear();
            }
        }
    }

    public boolean preventNullValueOfSaveDoctor() {
        if (txtName.getText().isEmpty() || txtContactNo.getText().isEmpty() || txtEmail.getText().isEmpty() ||
                cmbDepartmentName.getValue() == null || cmbSpecialist.getValue() == null) {

            new Alert(Alert.AlertType.INFORMATION, "Fill all the fields").show();
            return false;
        }

        if (!isValidEmail(txtEmail.getText())) {
            new Alert(Alert.AlertType.WARNING, "Enter a VALID EMAIL ADDRESS (e.g., moha@example.com)").show();
            return false;
        }

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

    public void setDoctorData(String name, String contactNo, String email,
                              String departmentName, String specialist) {
        txtName.setText(name);
        txtContactNo.setText(contactNo);
        txtEmail.setText(email);
        cmbDepartmentName.setValue(departmentName);
        cmbSpecialist.setValue(specialist);
    }

    public void clear() {
        txtName.clear();
        txtContactNo.clear();
        txtEmail.clear();
        cmbDepartmentName.setValue(null);
        cmbSpecialist.setValue(null);
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("DoctorManagementForm", context);
    }

    public void viewBtnOnAction(ActionEvent event) throws IOException {
        setUi("DoctorsViewAndManageAllForm", context);
    }
}
