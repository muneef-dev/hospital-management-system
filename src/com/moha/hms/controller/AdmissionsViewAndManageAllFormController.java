package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.*;
import com.moha.hms.dto.*;
import com.moha.hms.view.tm.AdmissionTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdmissionsViewAndManageAllFormController extends SuperBaseController {
    public BorderPane context;
    public TextField txtSearch;
    public TableView<AdmissionTm> tblAdmission;
    public TableColumn<AdmissionTm, Integer> colCount;
    public TableColumn<AdmissionTm, String> colId;
    public TableColumn<AdmissionTm, String> colPatientName;
    public TableColumn<AdmissionTm, String> colDoctorName;
    public TableColumn<AdmissionTm, String> colRoomName;
    public TableColumn<AdmissionTm, String> colAdmittingDate;
    public TableColumn<AdmissionTm, String> colDischargeDate;
    public TableColumn<AdmissionTm, String> colDiagnosisId;
    public TableColumn<AdmissionTm, Button> colUpdate;
    public TableColumn<AdmissionTm, Button> colDelete;
    public Button btnAddNew;
    public Button btnBack;

    private String searchText = "";
    private AdmissionBo admissionBo = BoFactory.getInstance().getBo(BoFactory.BoType.ADMISSION);
    PatientBo patientBo = BoFactory.getInstance().getBo(BoFactory.BoType.PATIENT);
    DoctorBo doctorBo = BoFactory.getInstance().getBo(BoFactory.BoType.DOCTOR);
    RoomBo roomBo = BoFactory.getInstance().getBo(BoFactory.BoType.ROOM);
    DiagnosisBo diagnosisBo = BoFactory.getInstance().getBo(BoFactory.BoType.DIAGNOSIS);
    private ObservableList<AdmissionTm> admissionTmObservableList = FXCollections.observableArrayList();

    public void initialize() throws SQLException, ClassNotFoundException {
        setTableColumns();
        loadAllAdmissions(searchText);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText = newValue;
            try {
                loadAllAdmissions(searchText);
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void setTableColumns() {
        colCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colDoctorName.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        colRoomName.setCellValueFactory(new PropertyValueFactory<>("roomName"));
        colAdmittingDate.setCellValueFactory(new PropertyValueFactory<>("admittingDate"));
        colDischargeDate.setCellValueFactory(new PropertyValueFactory<>("dischargeDate"));
        colDiagnosisId.setCellValueFactory(new PropertyValueFactory<>("diagnosisId"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("updateBtn"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("deleteBtn"));
    }

    public void loadAllAdmissions(String searchText) throws SQLException, ClassNotFoundException {
        int count = 0;
        admissionTmObservableList.clear();
        for (AdmissionDto admissionDto : (searchText.length() > 0) ? admissionBo.searchAdmission(searchText) : admissionBo.loadAllAdmissions()) {
            ImageView updateBtnImage = new ImageView(new Image("com/moha/hms/assets/icons/editing.png"));
            ImageView deleteBtnImage = new ImageView(new Image("com/moha/hms/assets/icons/trash.png"));

            updateBtnImage.setFitWidth(15);
            updateBtnImage.setFitHeight(15);
            deleteBtnImage.setFitWidth(15);
            deleteBtnImage.setFitHeight(15);

            Button updateBtn = new Button("", updateBtnImage);
            Button deleteBtn = new Button("", deleteBtnImage);
            count++;

            AdmissionTm admissionTm = new AdmissionTm(
                    String.valueOf(count), admissionDto.getId(), Date.valueOf(admissionDto.getAdmittingDate().toString()),
                    Date.valueOf(admissionDto.getDischargeDate().toString()), patientBo.getPatientName(admissionDto.getPatientName()),
                    roomBo.getRoomName(admissionDto.getRoomName()), doctorBo.getDoctorName(admissionDto.getDoctorName()),
                    admissionDto.getDiagnosisId(), updateBtn, deleteBtn
            );
            admissionTmObservableList.add(admissionTm);

            deleteBtn.setOnAction(event -> {
                try {
                    Alert alert = new Alert(
                            Alert.AlertType.CONFIRMATION,
                            "Are you sure to DELETE this admission?",
                            ButtonType.YES, ButtonType.NO
                    );
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get().equals(ButtonType.YES)) {
                        if (admissionBo.deleteAdmission(admissionDto.getId())) {
                            new Alert(Alert.AlertType.INFORMATION, "Admission deleted!").show();
                            loadAllAdmissions(searchText);
                        } else {
                            new Alert(Alert.AlertType.WARNING, "Try again").show();
                        }
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
                }
            });

            updateBtn.setOnAction(event -> {
                try {
                    Alert alert = new Alert(
                            Alert.AlertType.CONFIRMATION,
                            "Do you want to UPDATE this admission?",
                            ButtonType.YES, ButtonType.NO
                    );
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get().equals(ButtonType.YES)) {
                        setUiWithData("AdmissionAddNewForm", context, admissionDto);
                    }
                } catch (IOException | SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
                }
            });
        }
        tblAdmission.setItems(admissionTmObservableList);

        String userRoleName = getUserRoleName();
        if (userRoleName.equals("NURSE")){
            colDelete.setVisible(false);
        } else if (userRoleName.equals("DOCTOR")) {
            colUpdate.setVisible(false);
            colDelete.setVisible(false);
        }  else if (userRoleName.equals("RECEPTIONIST")) {
            colDelete.setVisible(false);
        }
    }

    public void setUiWithData(String location, BorderPane context, AdmissionDto admissionDto) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/" + location + ".fxml"));
        Parent root = loader.load();
        SuperBaseController controller = loader.getController();

        if (controller instanceof AdmissionAddNewFormController) {
            AdmissionAddNewFormController admissionAddNewFormController = (AdmissionAddNewFormController) controller;
            admissionAddNewFormController.saveBtn.setText("Update");
            admissionAddNewFormController.saveBtn.setOnAction(event -> {
                if (admissionAddNewFormController.preventNullValueOfSaveAdmission()) {
                    try {
//                        String doctor = (String) admissionAddNewFormController.txtDoctorName.getText();
//                        Optional<DepartmentDto> selectedDepartmentDto =
//                                admissionAddNewFormController.doctorDtoList.stream().filter(e -> e.getName().equals(department)).findFir
                        boolean updated = admissionBo.updateAdmission(new AdmissionDto(
                                admissionDto.getId(),
                                Date.valueOf(admissionAddNewFormController.txtAdmittingDate.getValue()),
                                Date.valueOf(admissionAddNewFormController.txtDischargeDate.getValue()),
                                admissionAddNewFormController.txtPatientName.getText(),
                                admissionAddNewFormController.cmbRoomName.getValue(),
                                admissionAddNewFormController.txtDoctorName.getText(),
                                "710"
                        ));
                        if (updated) {
                            new Alert(Alert.AlertType.INFORMATION, "Admission is Updated").show();
                            admissionAddNewFormController.clear();
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
                    }
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try again").show();
                }
            });
            admissionAddNewFormController.setAdmissionData(
                    admissionDto.getPatientName(), admissionDto.getRoomName(),
                    Date.valueOf(String.valueOf(admissionDto.getDischargeDate())).toLocalDate(),
                    admissionDto.getDoctorName(),
                    Date.valueOf(String.valueOf(admissionDto.getAdmittingDate())).toLocalDate()
            );
        }
        context.getChildren().clear();
        context.setCenter(root);
    }

    public void addNewBtnOnAction(ActionEvent event) throws IOException {
        setUi("AdmissionAddNewForm", context);
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("AdmissionManagementForm", context);
    }
}
