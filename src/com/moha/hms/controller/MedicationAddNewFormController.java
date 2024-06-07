package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.DiagnosisBo;
import com.moha.hms.bo.custom.InventoryItemBo;
import com.moha.hms.bo.custom.MedicationBo;
import com.moha.hms.bo.custom.PatientBo;
import com.moha.hms.dto.*;
import com.moha.hms.util.KeyGenerator;
import com.moha.hms.util.QrCodeGenerator;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class MedicationAddNewFormController extends SuperBaseController {
    public BorderPane context;
    public TextField txtPatientId;
    public TextField txtPatientName;

    public TextField txtQuantity;
    public TextField txtTotalCost;
    public Button btnCalculateTotalCost;
    public Button saveBtn;
    public TextField txtMedicationName;

    PatientBo patientBo = BoFactory.getInstance().getBo(BoFactory.BoType.PATIENT);
    DiagnosisBo diagnosisBo = BoFactory.getInstance().getBo(BoFactory.BoType.DIAGNOSIS);
    MedicationBo medicationBo = BoFactory.getInstance().getBo(BoFactory.BoType.MEDICATION);
    InventoryItemBo inventoryItemBo = BoFactory.getInstance().getBo(BoFactory.BoType.INVENTORY_ITEM);
    Set<String> suggestionPatientNamesSet = new HashSet<>();
    Set<String> suggestionInventoryItemNamesSet = new HashSet<>();

    List<PatientDto> patientDtoList = new ArrayList<>();
    List<InventoryItemDto> inventoryItemDtoList = new ArrayList<>();

    Optional<PatientDto> selectedPatientDto = Optional.empty();
    Optional<InventoryItemDto> selectedMedicationDto = Optional.empty();
    Integer sellingQty = 0;
    Integer qtyOnHand = 0;

    Double totalCost = 0.00;

    public void initialize(){
        saveBtn.setDisable(true);
        txtTotalCost.setDisable(true);
        try {
            loadAllPatients();
            loadAllInventoryItems();
            TextFields.bindAutoCompletion(txtPatientName, suggestionPatientNamesSet);
            TextFields.bindAutoCompletion(txtMedicationName, suggestionInventoryItemNamesSet);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadAllPatients() throws SQLException, ClassNotFoundException {
        patientDtoList = patientBo.loadAllPatients();
        suggestionPatientNamesSet = patientDtoList.stream()
                .map(PatientDto::getName)
                .collect(Collectors.toSet());
    }

    public void loadAllInventoryItems() throws SQLException, ClassNotFoundException {
        inventoryItemDtoList = inventoryItemBo.loadAllInventoryItems();
        suggestionInventoryItemNamesSet = inventoryItemDtoList.stream()
                .map(InventoryItemDto::getName)
                .collect(Collectors.toSet());
    }

    public void CalculateTotalCostBtnOnAction(ActionEvent event) {
        String patientNameText = txtPatientName.getText();
        selectedPatientDto = patientDtoList.stream().filter(e -> e.getName().equals(patientNameText)).findFirst();

        if (!selectedPatientDto.isPresent()) {
            new Alert(Alert.AlertType.WARNING, "Patient not found").show();
            return;
        }

        String inventoryItemNameText = txtMedicationName.getText();
        selectedMedicationDto = inventoryItemDtoList.stream().filter(e -> e.getName().equals(inventoryItemNameText)).findFirst();

        if (!selectedMedicationDto.isPresent()) {
            new Alert(Alert.AlertType.WARNING, "Medication not found").show();
            return;
        }

        if (txtQuantity.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Quantity field is empty").show();
            return;
        }
        if (!selectedMedicationDto.isPresent()) {
            new Alert(Alert.AlertType.WARNING, "Medication is not selected").show();
            return;
        }

        sellingQty = Integer.parseInt(txtQuantity.getText());
        qtyOnHand = selectedMedicationDto.get().getQtyOnHand() - sellingQty;

        if (qtyOnHand < 0) {
            new Alert(Alert.AlertType.WARNING, "Insufficient stock available").show();
            return;
        }

        totalCost = sellingQty * selectedMedicationDto.get().getSellingPrice();
        txtTotalCost.setText(totalCost.toString());
        saveBtn.setDisable(false);
        btnCalculateTotalCost.setDisable(true);
    }

    public void saveBtnOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (preventNullValueOfSaveMedication()) {
            inventoryItemBo.updateInventoryItem(new InventoryItemDto(
                    selectedMedicationDto.get().getId(), selectedMedicationDto.get().getName(),
                    selectedMedicationDto.get().getQrCode(), selectedMedicationDto.get().getCategory(),
                    qtyOnHand, selectedMedicationDto.get().getMinimumOrderQuantity(),
                    selectedMedicationDto.get().getSupplierName(), selectedMedicationDto.get().getBuyingPrice(),
                    selectedMedicationDto.get().getSellingPrice()
            ));

            List<DiagnosisDto> diagnosisDtoList = diagnosisBo.diagnosedPatient(selectedPatientDto.get().getId());
            List<String> diagnosisIds = diagnosisDtoList.stream().map(DiagnosisDto::getId).collect(Collectors.toList());
            String diagnosisIdsString = String.join(",", diagnosisIds);

            MedicationDto medicationDto = new MedicationDto(
                    KeyGenerator.generateId(), selectedPatientDto.get().getId(), txtPatientName.getText(),
                    sellingQty, totalCost, diagnosisIdsString, selectedMedicationDto.get().getId()
            );
            boolean medication = medicationBo.createMedication(medicationDto);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure about details of this medication?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.isPresent() && buttonType.get().equals(ButtonType.YES)) {
                if (medication) {
                    new Alert(Alert.AlertType.INFORMATION, "Medication is Saved").show();
                    clear();
                }
            }
        }
    }

    public void AddRecentBtnOnAction(ActionEvent event) {
    }

    public boolean preventNullValueOfSaveMedication() {
        if (txtPatientName.getText().isEmpty() ||
                txtMedicationName.getText().isEmpty() || txtQuantity.getText().isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION, "Fill all fields").show();
            return false;
        }
        if (!isNumeric(txtQuantity.getText())) {
            new Alert(Alert.AlertType.WARNING, "Enter NUMERIC VALUES for Quantity and Total cost").show();
            return false;
        }
        return true;
    }

    public void clearBtnOnAction(ActionEvent event) {
        clear();
    }

    public void setMedicationData(String patientId, String patientName, String medicationName,
                                  String quantity, String totalCost) {
        txtPatientId.setText(patientId);
        txtPatientName.setText(patientName);
        txtMedicationName.setText(medicationName);
        txtQuantity.setText(quantity);
        txtTotalCost.setText(totalCost);
    }

    public void clear() {
        txtPatientId.clear();
        txtPatientName.clear();
        txtMedicationName.clear();
        txtQuantity.clear();
        txtTotalCost.clear();
        btnCalculateTotalCost.setDisable(false);
        saveBtn.setDisable(true);
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("MedicationManagementForm", context);
    }

    public void viewBtnOnAction(ActionEvent event) throws IOException {
        setUi("MedicationsViewAndManageAllForm", context);
    }
}
