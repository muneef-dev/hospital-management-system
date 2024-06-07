package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.PatientBo;
import com.moha.hms.dto.PatientDto;
import com.moha.hms.view.tm.DoctorTm;
import com.moha.hms.view.tm.PatientTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ReportManagementFormController extends SuperBaseController {

    public BorderPane context;
    public Button btnPatient;

    public void btnPatientOnAction(ActionEvent event) throws IOException {
        setUi("ReportPatientsForm",context);
    }

    public void btnViewAndManageAllOnAction(ActionEvent event) {
        // Redirect to the report management form
    }
}
