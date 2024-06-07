package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.MedicationBo;
import com.moha.hms.dto.MedicationDto;
import com.moha.hms.view.tm.MedicationTm;
import com.moha.hms.view.tm.MedicationTm;
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
import java.sql.Date;
import java.sql.SQLException;
import java.util.Optional;

public class MedicationsViewAndManageAllFormController extends SuperBaseController {
    public BorderPane context;
    public TextField txtSearch;
    public Button btnAddNew;
    public Button btnBack;
    public TableView<MedicationTm> tblMedications;
    public TableColumn<MedicationTm, String> colCount;
    public TableColumn<MedicationTm, String> colId;
    public TableColumn<MedicationTm, String> colPatientId;
    public TableColumn<MedicationTm, String> colPatientName;
    public TableColumn<MedicationTm, String> colQuantity;
    public TableColumn<MedicationTm, String> colTotalCost;
    public TableColumn<MedicationTm, String> colDiagnosisId;
    public TableColumn<MedicationTm, String> colInventoryItemId;
    public TableColumn<MedicationTm, String> colUpdate;
    public TableColumn<MedicationTm, String> colDelete;

    private final MedicationBo medicationBo = BoFactory.getInstance().getBo(BoFactory.BoType.MEDICATION);
    private final ObservableList<MedicationTm> medicationTmObservableList = FXCollections.observableArrayList();
    private MedicationDto medicationDtoOuter;
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
        colPatientId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colPatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("soldQty"));
        colTotalCost.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        colDiagnosisId.setCellValueFactory(new PropertyValueFactory<>("diagnosisId"));
        colInventoryItemId.setCellValueFactory(new PropertyValueFactory<>("inventoryItemId"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("updateBtn"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("deleteBtn"));
    }

    public void loadAllData(String searchText) throws SQLException, ClassNotFoundException {
        int count = 0;
        medicationTmObservableList.clear();
        for (MedicationDto medicationDto : (searchText.length() > 0) ? medicationBo.searchMedication(searchText) : medicationBo.loadAllMedications()) {
            medicationDtoOuter = medicationDto;
            // Create ImageViews for the buttons
            ImageView updateBtnImage = createImageView("com/moha/hms/assets/icons/editing.png");
            ImageView deleteBtnImage = createImageView("com/moha/hms/assets/icons/trash.png");

            Button updateBtn = createButton(updateBtnImage);
            Button deleteBtn = createButton(deleteBtnImage);
            count++;
            MedicationTm medicationTm = new MedicationTm(
                    String.valueOf(count),medicationDto.getId(), medicationDto.getPatientId(),
                    medicationDto.getPatientName(), medicationDto.getSoldQty(), medicationDto.getTotalCost(),
                    medicationDto.getDiagnosisId(), medicationDto.getInventoryItemId(), updateBtn, deleteBtn
            );
            medicationTmObservableList.add(medicationTm);

            deleteBtn.setOnAction(event -> handleDeleteAction(medicationDto));

            updateBtn.setOnAction(event -> handleUpdateAction(medicationDto));
        }
        tblMedications.setItems(medicationTmObservableList);

        String userRoleName = getUserRoleName();
        if (userRoleName.equals("NURSE")||userRoleName.equals("DOCTOR")){
            colDelete.setVisible(false);
        }
    }

    public void setUiWithData(String location, BorderPane context, MedicationDto medicationDto) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/" + location + ".fxml"));
        Parent root = loader.load();
        SuperBaseController controller = loader.getController();

        if (controller instanceof MedicationAddNewFormController) {
            MedicationAddNewFormController medicationAddNewFormController = (MedicationAddNewFormController) controller;
            medicationAddNewFormController.saveBtn.setText("Update");
            medicationAddNewFormController.saveBtn.setOnAction(event -> {
                if (medicationAddNewFormController.preventNullValueOfSaveMedication()) {
                    try {
                        boolean medication = medicationBo.updateMedication(new MedicationDto(
                                medicationDto.getId(), medicationAddNewFormController.txtPatientId.getText(),
                                medicationAddNewFormController.txtPatientName.getText(),
                                Integer.parseInt(medicationAddNewFormController.txtQuantity.getText()),
                                Double.parseDouble(medicationAddNewFormController.txtTotalCost.getText()),
                                medicationDto.getDiagnosisId(), medicationDto.getInventoryItemId()
                        ));

                        if (medication) {
                            new Alert(Alert.AlertType.INFORMATION, "Medication is Updated").show();
                            medicationAddNewFormController.clear();
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.WARNING,e.getMessage()).show();
                    }
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try again").show();
                }
            });
            medicationAddNewFormController.setMedicationData(
                    medicationDto.getPatientId(), medicationDto.getPatientName(), medicationDto.getInventoryItemId(),
                    String.valueOf(medicationDto.getSoldQty()), String.valueOf(medicationDto.getTotalCost())
            );
        }
        context.getChildren().clear();
        context.setCenter(root);
    }

    private void handleDeleteAction(MedicationDto medicationDto) {
        try {
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Are you sure to DELETE this medication ???", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get().equals(ButtonType.YES)) {
                if (medicationBo.deleteMedication(medicationDto.getId())) {
                    new Alert(Alert.AlertType.INFORMATION, "Medication deleted !!!").show();
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

    private void handleUpdateAction(MedicationDto medicationDto) {
        try {
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Do you want to UPDATE this medication ???", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get().equals(ButtonType.YES)) {
                setUiWithData("MedicationAddNewForm", context, medicationDto);
            }
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
        }
    }

    public void addNewBtnOnAction(ActionEvent event) throws IOException {
        setUi("MedicationAddNewForm",context);
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("MedicationManagementForm",context);
    }
}
