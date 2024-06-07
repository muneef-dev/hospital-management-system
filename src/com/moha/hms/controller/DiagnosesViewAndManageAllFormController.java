package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.DiagnosisBo;
import com.moha.hms.bo.custom.DoctorBo;
import com.moha.hms.bo.custom.NurseBo;
import com.moha.hms.bo.custom.PatientBo;
import com.moha.hms.dto.DiagnosisDto;
import com.moha.hms.view.tm.DiagnosisTm;
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
import java.util.Date;
import java.util.Optional;

public class DiagnosesViewAndManageAllFormController extends SuperBaseController {

    public BorderPane context;
    public ImageView backBtnImg;
    public ImageView addBtnImg;
    public TableView<DiagnosisTm> tblDiagnoses;
    public TableColumn<DiagnosisTm, String> colCount;
    public TableColumn<DiagnosisTm, String> colId;
    public TableColumn<DiagnosisTm, String> colPatientName;
    public TableColumn<DiagnosisTm, String> colDoctorName;
    public TableColumn<DiagnosisTm, String> colNurseName;
    public TableColumn<DiagnosisTm, String> colDescription;
    public TableColumn<DiagnosisTm, String> colPatientType;
    public TableColumn<DiagnosisTm, String> colDate;
    public TableColumn<DiagnosisTm, String> colAppointmentDate;
    public TableColumn<DiagnosisTm, Button> colUpdate;
    public TableColumn<DiagnosisTm, Button> colDelete;
    public TextField txtSearch;

    private final DiagnosisBo diagnosisBo = BoFactory.getInstance().getBo(BoFactory.BoType.DIAGNOSIS);
    public Button btnBack;
    public Button btnAddNew;
    PatientBo patientBo = BoFactory.getInstance().getBo(BoFactory.BoType.PATIENT);
    DoctorBo doctorBo = BoFactory.getInstance().getBo(BoFactory.BoType.DOCTOR);
    NurseBo nurseBo = BoFactory.getInstance().getBo(BoFactory.BoType.NURSE);
    private final ObservableList<DiagnosisTm> diagnosisTmObservableList = FXCollections.observableArrayList();
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
        colPatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colDoctorName.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        colNurseName.setCellValueFactory(new PropertyValueFactory<>("nurseName"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPatientType.setCellValueFactory(new PropertyValueFactory<>("patientType"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colAppointmentDate.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("updateBtn"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("deleteBtn"));
    }

    public void loadAllData(String searchText) throws SQLException, ClassNotFoundException {
        int count = 0;
        diagnosisTmObservableList.clear();
        for (DiagnosisDto diagnosisDto : (searchText.length() > 0) ? diagnosisBo.searchDiagnosis(searchText) : diagnosisBo.loadAllDiagnosiss()) {
            // Create ImageViews for the buttons
            ImageView updateBtnImage = createImageView("com/moha/hms/assets/icons/editing.png");
            ImageView deleteBtnImage = createImageView("com/moha/hms/assets/icons/trash.png");

            Button updateBtn = createButton(updateBtnImage);
            Button deleteBtn = createButton(deleteBtnImage);
            count++;
            DiagnosisTm diagnosisTm = new DiagnosisTm(
                    String.valueOf(count), diagnosisDto.getId(), diagnosisDto.getDescription(),
                    diagnosisDto.getDate(), diagnosisDto.getPatientType(), diagnosisDto.getAppointmentId(),
                    patientBo.getPatientName(diagnosisDto.getPatientName()),
                    doctorBo.getDoctorName(diagnosisDto.getDoctorName()),
                    nurseBo.getNurseName(diagnosisDto.getNurseName()),
                    updateBtn, deleteBtn
            );
            diagnosisTmObservableList.add(diagnosisTm);

            deleteBtn.setOnAction(event -> handleDeleteAction(diagnosisDto));
            updateBtn.setOnAction(event -> handleUpdateAction(diagnosisDto));
        }
        tblDiagnoses.setItems(diagnosisTmObservableList);

        String userRoleName = getUserRoleName();
        if (userRoleName.equals("NURSE")){
            colUpdate.setVisible(false);
            colDelete.setVisible(false);
        } else if (userRoleName.equals("DOCTOR")) {
            colDelete.setVisible(false);
        }  else if (userRoleName.equals("RECEPTIONIST")) {
            colUpdate.setVisible(false);
            colDelete.setVisible(false);
        }
    }

    public void setUiWithData(String location, BorderPane context, DiagnosisDto diagnosisDto) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/" + location + ".fxml"));
        Parent root = loader.load();
        SuperBaseController controller = loader.getController();

        if (controller instanceof DiagnosisAddNewFormController) {
            DiagnosisAddNewFormController diagnosisAddNewFormController = (DiagnosisAddNewFormController) controller;
            diagnosisAddNewFormController.saveBtn.setText("Update");
//            diagnosisAddNewFormController.setDiagnosisData(diagnosisDto);
            diagnosisAddNewFormController.saveBtn.setOnAction(event -> {
                if (diagnosisAddNewFormController.preventNullValueOfSaveDiagnosis()) {
                    try {
                        boolean isUpdated = diagnosisBo.updateDiagnosis(diagnosisDto);
                        if (isUpdated) {
                            new Alert(Alert.AlertType.INFORMATION, "Diagnosis is Updated").show();
                            diagnosisAddNewFormController.clear();
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
                    }
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try again").show();
                }
            });
        }
        context.getChildren().clear();
        context.setCenter(root);
    }

    private void handleDeleteAction(DiagnosisDto diagnosisDto) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to DELETE this diagnosis?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get().equals(ButtonType.YES)) {
                if (diagnosisBo.deleteDiagnosis(diagnosisDto.getId())) {
                    new Alert(Alert.AlertType.INFORMATION, "Diagnosis deleted!").show();
                    loadAllData(searchText);
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try again").show();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
        }
    }

    private void handleUpdateAction(DiagnosisDto diagnosisDto) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to UPDATE this diagnosis?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get().equals(ButtonType.YES)) {
                setUiWithData("DiagnosisAddNewForm", context, diagnosisDto);
            }
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
        }
    }

    public void addNewBtnOnAction(ActionEvent event) throws IOException {
        setUi("DiagnosisAddNewForm", context);
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("DiagnosisManagementForm", context);
    }
}
