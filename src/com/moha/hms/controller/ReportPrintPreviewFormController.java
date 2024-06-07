package com.moha.hms.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class ReportPrintPreviewFormController extends SuperBaseController {
    public BorderPane context;
    public Label lblId;
    public Label lblName;
    public Label lblNic;
    public Label lblDob;
    public Label lblAge;
    public Label lblGender;
    public Label lblEmail;
    public Label lblAddress;
    public Label lblContact;
    public Label lblEmergencyContact;
    public Button printBtn;

    public void initialize(){
    }

    public void setPrintPreviewData(String id,String name, String nic, String dob, String age
    , String gender, String email, String address, String contact, String emerCont){
        lblId.setText(id);
        lblName.setText(name);
        lblNic.setText(nic);
        lblDob.setText(dob);
        lblAge.setText(age);
        lblGender.setText(gender);
        lblEmail.setText(email);
        lblAddress.setText(address);
        lblContact.setText(contact);
        lblEmergencyContact.setText(emerCont);
    }

    public void printBtnOnAction(ActionEvent event) {
        new Alert(Alert.AlertType.INFORMATION, "Coming soon").show();
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("ReportPatientsForm",context);
    }
}
