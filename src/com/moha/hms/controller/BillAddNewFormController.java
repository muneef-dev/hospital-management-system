package com.moha.hms.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.*;
import com.moha.hms.dto.BillDto;
import com.moha.hms.dto.DiagnosisDto;
import com.moha.hms.dto.MedicationDto;
import com.moha.hms.dto.PatientDto;
import com.moha.hms.util.KeyGenerator;
import com.moha.hms.util.QrCodeGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import org.apache.commons.codec.binary.Base64;
import org.controlsfx.control.textfield.TextFields;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class BillAddNewFormController extends SuperBaseController {
    public BorderPane context;
    public ImageView backBtnImg;
    public ImageView viewBtnImg;
    public ImageView saveBtnImg;
    public ImageView clearBtnImg;
    public ComboBox<String> cmbPaymentStatus;
    public DatePicker txtIssueDate;
    public TextField txtDoctorFee;
    public TextField txtMedicationCost;
    public TextField txtLabTestCost;
    public TextField txtOtherCost;
    public ImageView imgQrCode;
    @FXML
    public Button saveBtn;
    public TextField txtPatientName;

    BillBo billBo = BoFactory.getInstance().getBo(BoFactory.BoType.BILL);
    PatientBo patientBo = BoFactory.getInstance().getBo(BoFactory.BoType.PATIENT);
    DiagnosisBo diagnosisBo = BoFactory.getInstance().getBo(BoFactory.BoType.DIAGNOSIS);
    MedicationBo medicationBo = BoFactory.getInstance().getBo(BoFactory.BoType.MEDICATION);
    UserBo userBo = BoFactory.getInstance().getBo(BoFactory.BoType.USER);
    ObservableList<String> observableListPaymentStatus = FXCollections.observableArrayList();
    Set<String> suggestionPatientNamesSet = new HashSet<>();

    String qrCodeUniqueData = null;
    BufferedImage bufferedImage = null;

    public void initialize() {
        try {
            setQrCode();
            loadAllPatients();
            loadAllPaymentStatuses();
            TextFields.bindAutoCompletion(txtPatientName, suggestionPatientNamesSet);
        } catch (SQLException | ClassNotFoundException | WriterException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadAllPatients() throws SQLException, ClassNotFoundException {
        ArrayList<String> patientNamesList = new ArrayList<>();
        for (PatientDto patientDto : patientBo.loadAllPatients()) {
            patientNamesList.add(patientDto.getName());
        }
        suggestionPatientNamesSet = new HashSet<>(patientNamesList);
    }

    public void loadAllPaymentStatuses() {
        observableListPaymentStatus.clear();
        observableListPaymentStatus.addAll("PAID", "UNPAID");
        cmbPaymentStatus.setItems(observableListPaymentStatus);
        cmbPaymentStatus.setValue("UNPAID");
    }

    public void saveBtnOnAction(ActionEvent event) throws SQLException, ClassNotFoundException, IOException, WriterException {
        if (preventNullValueOfSaveBill()) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            javax.imageio.ImageIO.write(bufferedImage,"png", byteArrayOutputStream); // format name -> jpeg/png
            byte[] arr = byteArrayOutputStream.toByteArray();

            List<PatientDto> patientDtoList = patientBo.loadAllPatients();
            String patientNameText = txtPatientName.getText();
            Optional<PatientDto> selectedPatientDto =
                    patientDtoList.stream().filter(e -> e.getName().equals(patientNameText)).findFirst();

            List<DiagnosisDto> diagnosisDtoList = diagnosisBo.diagnosedPatient(selectedPatientDto.get().getId()); // fix error no patient under that name
            List<String> diagnosisIds = diagnosisDtoList.stream()
                    .map(DiagnosisDto::getId)
                    .collect(Collectors.toList());

            String diagnosisIdsString = String.join(",", diagnosisIds);

            List<MedicationDto> medicationAvailableForPatient = medicationBo.isMedicationAvailableForPatient(diagnosisIdsString);
            //List<MedicationDto> medicationAvailableForPatient = medicationBo.isMedicationAvailableForPatient("710");

            System.out.println(medicationAvailableForPatient);

            List<String> medicationIds = medicationAvailableForPatient.stream()
                    .map(MedicationDto::getId)
                    .collect(Collectors.toList());

            String medicationIdsString = String.join(",", medicationIds);

            BillDto billDto = new BillDto(
                    KeyGenerator.generateId(), Base64.encodeBase64String(arr),
                    Date.valueOf(txtIssueDate.getValue()),
                    String.valueOf(cmbPaymentStatus.getValue()),
                    Double.parseDouble(txtDoctorFee.getText()),
                    Double.parseDouble(txtMedicationCost.getText()),
                    Double.parseDouble(txtLabTestCost.getText()),
                    Double.parseDouble(txtOtherCost.getText()),
                    selectedPatientDto.get().getId(), medicationIdsString, getCurrentUser().getId()
            );

            System.out.println(diagnosisIds);
            System.out.println("medicationIdsString"+medicationIdsString);
            System.out.println(billDto);

            boolean bill = billBo.createBill(billDto);

            if (bill) {
                setQrCode();
                new Alert(Alert.AlertType.INFORMATION, "Bill is Saved").show();
                clear();
            }
        }
    }

    private void setQrCode() throws WriterException {
        qrCodeUniqueData = QrCodeGenerator.generate(25);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        bufferedImage = MatrixToImageWriter.toBufferedImage(qrCodeWriter.encode(qrCodeUniqueData, BarcodeFormat.QR_CODE,201,205));

        Image image = SwingFXUtils.toFXImage(bufferedImage,null); // toFXImage(BufferedImage bimg, WritableImage wimg) // parameters
        imgQrCode.setImage(image);
    }

    public boolean preventNullValueOfSaveBill() {
        if (txtPatientName.getText().isEmpty() || txtIssueDate.getValue() == null ||
                txtDoctorFee.getText().isEmpty() || txtMedicationCost.getText().isEmpty() ||
                txtLabTestCost.getText().isEmpty() || txtOtherCost.getText().isEmpty() ||
                cmbPaymentStatus.getValue() == null) {

            new Alert(Alert.AlertType.INFORMATION, "Fill all the fields").show();
            return false;
        }
        if (!isNumeric(txtDoctorFee.getText()) || !isNumeric(txtMedicationCost.getText()) ||
                !isNumeric(txtLabTestCost.getText()) || !isNumeric(txtOtherCost.getText())) {
            new Alert(
                    Alert.AlertType.WARNING,
                    "Enter NUMERIC VALUES for costs").show();
            return false;
        }
        return true;
    }

    public void clearBtnOnAction(ActionEvent event) {
        clear();
    }

    public void clear() {
        txtPatientName.setText(null);
        cmbPaymentStatus.setValue(null);
        txtIssueDate.setValue(null);
        txtDoctorFee.clear();
        txtMedicationCost.clear();
        txtLabTestCost.clear();
        txtOtherCost.clear();
    }

    public void setBillData(String patientName, String paymentStatus, LocalDate issueDate,
                            String doctorFee, String medicationCost, String labTestCost, String otherCost, String qrCode) {
        txtPatientName.setText(patientName);
        cmbPaymentStatus.setValue(paymentStatus);
        txtIssueDate.setValue(issueDate);
        txtDoctorFee.setText(doctorFee);
        txtMedicationCost.setText(medicationCost);
        txtLabTestCost.setText(labTestCost);
        txtOtherCost.setText(otherCost);
        byte[] data = Base64.decodeBase64(qrCode);
        imgQrCode.setImage(new Image(new ByteArrayInputStream(data)));
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("BillManagementForm", context);
    }

    public void viewBtnOnAction(ActionEvent event) throws IOException {
        setUi("BillsViewAndManageAllForm", context);
    }

    public void AddRecentBtnOnAction(ActionEvent event) {
        // Your code for AddRecentBtnOnAction here
    }
}
