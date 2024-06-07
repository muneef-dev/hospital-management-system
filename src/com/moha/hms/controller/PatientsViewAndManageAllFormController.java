package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.PatientBo;
import com.moha.hms.dto.PatientDto;
import com.moha.hms.view.tm.PatientTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Optional;

public class PatientsViewAndManageAllFormController extends SuperBaseController {

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
    public TableColumn<PatientTm, Button> colUpdate;
    public TableColumn<PatientTm, Button> colDelete;

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

        String role = getUserRoleName();
        if (role.equals("DOCTOR")||role.equals("NURSE")){
            btnAddNew.setVisible(false);
            btnBack.setVisible(false);
        }
    }

    public void setTableColumns(){
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
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("updateBtn"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("deleteBtn"));
    }

    public void loadAllData(String searchText) throws SQLException, ClassNotFoundException {
        int count = 0;
        patientTmObservableList.clear();
        for (PatientDto patientDto : (searchText.length() > 0) ? patientBo.searchPatient(searchText) : patientBo.loadAllPatients()) {
            patientDtoOuter = patientDto;
            // Create ImageViews for the buttons
            ImageView updateBtnImage = createImageView("com/moha/hms/assets/icons/editing.png");
            ImageView deleteBtnImage = createImageView("com/moha/hms/assets/icons/trash.png");

            Button updateBtn = createButton(updateBtnImage);
            Button deleteBtn = createButton(deleteBtnImage);
            count++;
            PatientTm patientTm = new PatientTm(
                    String.valueOf(count),patientDto.getId(), patientDto.getNic(), patientDto.getQrCode(),
                    patientDto.getName(), patientDto.getDob(), patientDto.getAge(),
                    patientDto.getGender(), patientDto.getAddress(), patientDto.getEmail(),
                    patientDto.getContactNo(), patientDto.getEmergencyContactNo(), updateBtn, deleteBtn
            );
            patientTmObservableList.add(patientTm);

            deleteBtn.setOnAction(event -> handleDeleteAction(patientDto));

            updateBtn.setOnAction(event -> handleUpdateAction(patientDto));
        }
        tblPatients.setItems(patientTmObservableList);

        String userRoleName = getUserRoleName();
        if (userRoleName.equals("NURSE")||userRoleName.equals("DOCTOR")){
            colNic.setVisible(false);
            colDob.setVisible(false);
            colAddress.setVisible(false);
            colEmail.setVisible(false);
            colContact.setVisible(false);
            colEmergency.setVisible(false);
            colUpdate.setVisible(false);
            colDelete.setVisible(false);
        } else if (userRoleName.equals("RECEPTIONIST")) {
            colDelete.setVisible(false);
        }
    }

    public void setUiWithData(String location, BorderPane context, PatientDto patientDto) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/" + location + ".fxml"));
        Parent root = loader.load();
        SuperBaseController controller = loader.getController();

        if (controller instanceof PatientAddNewFormController) {
            PatientAddNewFormController patientAddNewFormController = (PatientAddNewFormController) controller;
            patientAddNewFormController.saveBtn.setText("Update");
            patientAddNewFormController.saveBtn.setOnAction(event -> {
                if (patientAddNewFormController.preventNullValueOfSavePatient()) {
                    try {
                        boolean patient = patientBo.updatePatient(new PatientDto(
                                patientDto.getId(), patientAddNewFormController.txtNic.getText(), patientDto.getQrCode(),
                                patientAddNewFormController.txtName.getText(),
                                Date.valueOf(patientAddNewFormController.txtDob.getValue()),
                                String.valueOf(patientAddNewFormController.calculateAge(patientAddNewFormController.txtDob.getValue())),
                                String.valueOf(patientAddNewFormController.txtGender.getValue()),
                                patientAddNewFormController.txtAddress.getText(), patientAddNewFormController.txtEmail.getText(),
                                patientAddNewFormController.txtContactNo.getText(), patientAddNewFormController.txtEmergencyContactNo.getText()
                        ));

                        if (patient) {
                            new Alert(Alert.AlertType.INFORMATION, "Patient is Updated").show();
                            patientAddNewFormController.clear();
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.WARNING,e.getMessage()).show();
                    }
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try again").show();
                }
            });
            patientAddNewFormController.setPatientData(
                    patientDto.getName(), patientDto.getNic(), patientDto.getAddress(),
                    Date.valueOf(String.valueOf(patientDto.getDob())).toLocalDate(), patientDto.getGender(),
                    patientDto.getEmail(), patientDto.getContactNo(), patientDto.getEmergencyContactNo()
            );
        }
        context.getChildren().clear();
        context.setCenter(root);
    }

    private void handleDeleteAction(PatientDto patientDto) {
        try {
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Are you sure to DELETE this patient ???", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get().equals(ButtonType.YES)) {
                if (patientBo.deletePatient(patientDto.getId())) {
                    new Alert(Alert.AlertType.INFORMATION, "Patient deleted !!!").show();
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

    private void handleUpdateAction(PatientDto patientDto) {
        try {
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Do you want to UPDATE this patient ???", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get().equals(ButtonType.YES)) {
                setUiWithData("PatientAddNewForm", context, patientDto);
            }
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
        }
    }

    public void addNewBtnOnAction(ActionEvent event) throws IOException {
        setUi("PatientAddNewForm",context);
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("PatientManagementForm",context);
    }
}
