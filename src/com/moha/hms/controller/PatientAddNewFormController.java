package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.PatientBo;
import com.moha.hms.dto.PatientDto;
import com.moha.hms.util.KeyGenerator;
import com.moha.hms.util.QrCodeGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

public class PatientAddNewFormController extends SuperBaseController {
    public BorderPane context;
    public ImageView backBtnImg;
    public ImageView viewBtnImg;
    public ImageView saveBtnImg;
    public ImageView clearBtnImg;
    public TextField txtName;
    public DatePicker txtDob;
    public ComboBox txtGender;
    public TextField txtNic;
    public TextField txtAge;
    public TextField txtContactNo;
    public TextField txtAddress;
    public TextField txtEmail;
    public TextField txtEmergencyContactNo;
    @FXML
    public Button saveBtn;

    PatientBo patientBo = BoFactory.getInstance().getBo(BoFactory.BoType.PATIENT);
    ObservableList<String> observableListGenders = FXCollections.observableArrayList();

    public void initialize() {
        loadAllGenders();
    }

    public void loadAllGenders() {
        observableListGenders.clear();
        observableListGenders.addAll("MALE", "FEMALE");
        txtGender.setItems(observableListGenders);
        txtGender.setValue("MALE");
    }

    public void saveBtnOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (preventNullValueOfSavePatient()) {
            PatientDto patientDto = new PatientDto(
                    KeyGenerator.generateId(), txtNic.getText(), QrCodeGenerator.generate(25),
                    txtName.getText(), Date.valueOf(txtDob.getValue()),
                    String.valueOf(calculateAge(txtDob.getValue())), String.valueOf(txtGender.getValue()),
                    txtAddress.getText(), txtEmail.getText(), txtContactNo.getText(), txtEmergencyContactNo.getText()
            );
            boolean patient = patientBo.createPatient(patientDto);
                Alert alert = new Alert(
                        Alert.AlertType.CONFIRMATION,
                        "Are you sure about details of this patient ???", ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> buttonType = alert.showAndWait();
                if (buttonType.get().equals(ButtonType.YES)) {
                    if (patient){
                        SuperBaseController.setCurrentPatient(patientDto);
                        new Alert(Alert.AlertType.INFORMATION,"Patient is Saved").show();
                        clear();
                    }
                }
        }
    }

    public boolean preventNullValueOfSavePatient() {
        if (txtNic.getText().isEmpty() || txtName.getText().isEmpty() || txtDob.getValue() == null ||
                txtGender.getValue() == null || txtAddress.getText().isEmpty() || txtEmail.getText().isEmpty() ||
                txtContactNo.getText().isEmpty() || txtEmergencyContactNo.getText().isEmpty()) {

            new Alert(Alert.AlertType.INFORMATION, "Fill the all fields").show();
            return false;
        }

        if (!isValidEmail(txtEmail.getText())) {
            new Alert(Alert.AlertType.WARNING, "Enter a VALID EMAIL ADDRESS (e.g., moha@example.com)").show();
            return false;
        }

        if (!isNumeric(txtContactNo.getText()) || !isNumeric(txtEmergencyContactNo.getText())) {
            new Alert(
                    Alert.AlertType.WARNING,
                    "Enter NUMERIC VALUES for Contact Number and Emergency Contact Number").show();
            return false;
        } else if (!isValidPhoneNumber(txtContactNo.getText()) || !isValidPhoneNumber(txtEmergencyContactNo.getText())) {
            new Alert(Alert.AlertType.WARNING,
                    "Enter a VALID PHONE NUMBER (You entered less than or greater than 10 digit)").show();
            return false;
        }
        return true;
    }

    public int calculateAge(LocalDate dob) {
        if (dob != null) {
            LocalDate currentDate = LocalDate.now();
            return Period.between(dob, currentDate).getYears();
        }
        return 0;
    }

    public void clearBtnOnAction(ActionEvent event) {
        clear();
    }

    public void setPatientData(String name, String nic, String address, LocalDate dob,
                               String gender, String email, String contactNo, String emergencyContactNo) {
        txtName.setText(name);
        txtNic.setText(nic);
        txtAddress.setText(address);
        txtDob.setValue(dob);
        txtGender.setValue(gender);
        txtEmail.setText(email);
        txtContactNo.setText(contactNo);
        txtEmergencyContactNo.setText(emergencyContactNo);
    }

    public void clear(){
        txtName.clear();
        txtDob.setValue(null);
        txtGender.getItems().clear();
        txtNic.clear();
        txtAge.clear();
        txtContactNo.clear();
        txtAddress.clear();
        txtEmail.clear();
        txtEmergencyContactNo.clear();
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("PatientManagementForm",context);
    }

    public void viewBtnOnAction(ActionEvent event) throws IOException {
        setUi("PatientsViewAndManageAllForm",context);
    }
}
