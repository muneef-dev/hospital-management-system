package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.PatientBo;
import com.moha.hms.dto.InventoryItemDto;
import com.moha.hms.dto.PatientDto;
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
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;

public class ReportPatientFormController extends SuperBaseController {
    public BorderPane context;
    public ImageView backBtnImg;
    public ImageView addBtnImg;
    public TableView<PatientTm> tblPatients;
    public TableColumn<PatientTm, String> colCount;
    public TableColumn<PatientTm, String> colId;
    public TableColumn<PatientTm, String> colNic;
    public TableColumn<PatientTm, String> colName;
    public TableColumn<PatientTm, String> colDob;
    public TableColumn<PatientTm, Integer> colAge;
    public TableColumn<PatientTm, String> colGender;
    public TableColumn<PatientTm, String> colAddress;
    public TableColumn<PatientTm, String> colEmail;
    public TableColumn<PatientTm, String> colContact;
    public TableColumn<PatientTm, String> colEmergency;
    public TableColumn<PatientTm, Button> colPrint;

    public TextField txtSearch;
    public Button btnAddNew;
    public Button btnBack;

    private final PatientBo patientBo = BoFactory.getInstance().getBo(BoFactory.BoType.PATIENT);
    private final ObservableList<PatientTm> patientTmObservableList = FXCollections.observableArrayList();
    private PatientDto patientDtoOuter;
    private String searchText = "";

    public void initialize() throws SQLException, ClassNotFoundException {
        setTableColumns();
        loadAllData(searchText);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText = newValue;
            try {
                loadAllData(searchText);
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void setTableColumns() {
        colCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        colEmergency.setCellValueFactory(new PropertyValueFactory<>("emergencyContactNo"));
        colPrint.setCellValueFactory(new PropertyValueFactory<>("updateBtn"));
    }

    public void loadAllData(String searchText) throws SQLException, ClassNotFoundException {
        int count = 0;
        patientTmObservableList.clear();
        for (PatientDto patientDto : (searchText.length() > 0) ? patientBo.searchPatient(searchText) : patientBo.loadAllPatients()) {
            patientDtoOuter = patientDto;
            // Create ImageViews for the buttons
            ImageView printBtnImage = createImageView("com/moha/hms/assets/icons/icons8-print-100.png");

            Button printBtn = createButton(printBtnImage);
            count++;
            PatientTm patientTm = new PatientTm(
                    String.valueOf(count), patientDto.getId(), patientDto.getNic(), patientDto.getQrCode(),
                    patientDto.getName(), patientDto.getDob(), patientDto.getAge(),
                    patientDto.getGender(), patientDto.getAddress(), patientDto.getEmail(),
                    patientDto.getContactNo(), patientDto.getEmergencyContactNo(), printBtn, null
            );
            patientTmObservableList.add(patientTm);

            printBtn.setOnAction(event -> {
                try {
                    handlePrintAction(patientDto);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        }
        tblPatients.setItems(patientTmObservableList);
    }

    public void setUiWithData(String location, BorderPane context, PatientDto patientDto) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/" + location + ".fxml"));
        Parent root = loader.load();
        SuperBaseController controller = loader.getController();

        if (controller instanceof ReportPrintPreviewFormController) {
            ReportPrintPreviewFormController reportPrintPreviewFormController = (ReportPrintPreviewFormController) controller;
            reportPrintPreviewFormController.setPrintPreviewData(
                    patientDto.getId(), patientDto.getName(), patientDto.getNic(),
                    String.valueOf(patientDto.getDob()), patientDto.getAge(), patientDto.getGender(),
                    patientDto.getEmail(), patientDto.getAddress(),
                    patientDto.getContactNo(), patientDto.getEmergencyContactNo()
            );
        }
        context.getChildren().clear();
        context.setCenter(root);
    }

    private void handlePrintAction(PatientDto patientDto) throws IOException {
        if (patientDto == null) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Patient data is missing. Cannot proceed with print action.", ButtonType.OK);
            errorAlert.showAndWait();
            return;
        }

        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to print this patient?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.isPresent() && buttonType.get().equals(ButtonType.YES)) {
//            System.out.println("Patient ID: " + patientDto.getId());
//            System.out.println("NIC: " + patientDto.getNic());
//            System.out.println("Name: " + patientDto.getName());
//            System.out.println("DOB: " + patientDto.getDob());
//            System.out.println("Age: " + patientDto.getAge());
//            System.out.println("Gender: " + patientDto.getGender());
//            System.out.println("Address: " + patientDto.getAddress());
//            System.out.println("Email: " + patientDto.getEmail());
//            System.out.println("Contact No: " + patientDto.getContactNo());
//            System.out.println("Emergency Contact No: " + patientDto.getEmergencyContactNo());

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("id", patientDto.getId());
            parameters.put("nic", patientDto.getNic());
            parameters.put("name", patientDto.getName());
            parameters.put("dob", patientDto.getDob());
            parameters.put("email", patientDto.getEmail());
            parameters.put("age", patientDto.getAge());
            parameters.put("gender", patientDto.getGender());
            parameters.put("address", patientDto.getAddress());
            parameters.put("contactNo", patientDto.getContactNo());
            parameters.put("emergencyContactNo", patientDto.getEmergencyContactNo());

            // Load and compile the JasperReport template
            InputStream report = getClass().getResourceAsStream("/com/moha/hms/PatientReport_A4.jrxml");
            if (report == null) {
                throw new IOException("Report template not found");
            }
            JasperReport jasperReport;
            try {
                jasperReport = JasperCompileManager.compileReport(report);
            } catch (JRException e) {
                throw new RuntimeException("Error compiling JasperReport template", e);
            }

            // Fill the report with data
            try {
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setVisible(true);
            } catch (JRException e) {
                throw new RuntimeException("Error filling JasperReport with data", e);
            }
        }
    }





    public void addNewBtnOnAction(ActionEvent event) throws IOException {
        //setUi("PatientAddNewForm",context);
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("ReportManagementForm",context);
    }
}
