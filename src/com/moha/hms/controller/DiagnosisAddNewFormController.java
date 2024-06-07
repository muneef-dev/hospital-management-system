package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.*;
import com.moha.hms.dto.*;
import com.moha.hms.util.KeyGenerator;
import com.moha.hms.util.QrCodeGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class DiagnosisAddNewFormController extends SuperBaseController {
    public BorderPane context;
    public ImageView backBtnImg;
    public ImageView viewBtnImg;
    public ImageView saveBtnImg;
    public ImageView clearBtnImg;
    public TextField txtPatientName;
    public TextField txtNurseName;
    public DatePicker txtDate;
    public TextField txtDoctorName;
    public ComboBox<String> cmbPatientType;
    public TextField txtDescription;
    public DatePicker txtDiagnosedDate;
    @FXML
    public Button saveBtn;

    DiagnosisBo diagnosisBo = BoFactory.getInstance().getBo(BoFactory.BoType.DIAGNOSIS);
    AppointmentBo appointmentBo = BoFactory.getInstance().getBo(BoFactory.BoType.APPOINTMENT);
    DoctorBo doctorBo = BoFactory.getInstance().getBo(BoFactory.BoType.DOCTOR);
    PatientBo patientBo = BoFactory.getInstance().getBo(BoFactory.BoType.PATIENT);
    NurseBo nurseBo = BoFactory.getInstance().getBo(BoFactory.BoType.NURSE);
    ObservableList<String> observableListPatientTypes = FXCollections.observableArrayList();
    Set<String> suggestionDoctorNamesSet = new HashSet<>();
    Set<String> suggestionNurseNamesSet = new HashSet<>();
    Set<String> suggestionPatientNamesSet = new HashSet<>();
    List<PatientDto> patientDtoList = new ArrayList<>();
    List<DoctorDto> doctorDtoList = new ArrayList<>();
    List<NurseDto> nurseDtoList = new ArrayList<>();

    public void initialize() {
        try {
            loadAllPatientTypes();
            loadAllDoctors();
            loadAllPatients();
            loadAllNurses();
            TextFields.bindAutoCompletion(txtDoctorName, suggestionDoctorNamesSet);
            TextFields.bindAutoCompletion(txtNurseName, suggestionNurseNamesSet);
            TextFields.bindAutoCompletion(txtPatientName, suggestionPatientNamesSet);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadAllPatientTypes() {
        observableListPatientTypes.clear();
        observableListPatientTypes.addAll("IN", "OUT");
        cmbPatientType.setItems(observableListPatientTypes);
        cmbPatientType.setValue("IN");
    }

    public void loadAllDoctors() throws SQLException, ClassNotFoundException {
        ArrayList<String> doctorNamesList = new ArrayList<>();
        doctorDtoList = doctorBo.loadAllDoctors();
        for (DoctorDto doctorDto : doctorDtoList) {
            doctorNamesList.add(doctorDto.getName());
        }
        suggestionDoctorNamesSet = new HashSet<>(doctorNamesList);
    }

    public void loadAllPatients() throws SQLException, ClassNotFoundException {
        ArrayList<String> patientNamesList = new ArrayList<>();
        patientDtoList = patientBo.loadAllPatients();
        for (PatientDto patientDto : patientDtoList) {
            patientNamesList.add(patientDto.getName());
        }
        suggestionPatientNamesSet = new HashSet<>(patientNamesList);
    }

    public void loadAllNurses() throws SQLException, ClassNotFoundException {
        ArrayList<String> nurseNamesList = new ArrayList<>();
        nurseDtoList = nurseBo.loadAllNurses();
        for (NurseDto nurseDto : nurseDtoList) {
            nurseNamesList.add(nurseDto.getName());
        }
        suggestionNurseNamesSet = new HashSet<>(nurseNamesList);
    }

    public void saveBtnOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (preventNullValueOfSaveDiagnosis()) {
            String patientNameText = txtPatientName.getText();
            String doctorNameText = txtDoctorName.getText();
            String nurseNameText = txtNurseName.getText();

            Optional<PatientDto> selectedPatientDto = patientDtoList.stream()
                    .filter(e -> e.getName().equals(patientNameText))
                    .findFirst();
            Optional<DoctorDto> selectedDoctorDto = doctorDtoList.stream()
                    .filter(e -> e.getName().equals(doctorNameText))
                    .findFirst();
            Optional<NurseDto> selectedNurseDto = nurseDtoList.stream()
                    .filter(e -> e.getName().equals(nurseNameText))
                    .findFirst();

            if (selectedPatientDto.isPresent() && selectedDoctorDto.isPresent() && selectedNurseDto.isPresent()) {
                LocalDate localDate = txtDiagnosedDate.getValue();
                if (localDate != null) {
                    Date sqlDate = Date.valueOf(localDate);

                    DiagnosisDto diagnosisDto = new DiagnosisDto(
                            KeyGenerator.generateId(), txtDescription.getText(), sqlDate,
                            String.valueOf(cmbPatientType.getValue()), String.valueOf(getCurrentAppointment().getId()),
                            selectedPatientDto.get().getId(), selectedDoctorDto.get().getId(),
                            selectedNurseDto.get().getId()
                    );



                    System.out.println(diagnosisDto);

                    boolean diagnosis = diagnosisBo.createDiagnosis(diagnosisDto);

                    if (diagnosis) {
                        SuperBaseController.setCurrentDiagnosis(diagnosisDto);
                        new Alert(Alert.AlertType.INFORMATION, "Diagnosis is Saved").show();
                        clear();
                    }
                } else {
                    new Alert(Alert.AlertType.ERROR, "Please select a date.").show();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Selected patient, doctor, or nurse not found.").show();
            }
        }
    }

    public boolean preventNullValueOfSaveDiagnosis() {
        if (txtPatientName.getText().isEmpty() || txtNurseName.getText().isEmpty() || txtDoctorName.getText().isEmpty() ||
                txtDiagnosedDate.getValue() == null || txtDescription.getText().isEmpty() || cmbPatientType.getValue() == null) {

            new Alert(Alert.AlertType.INFORMATION, "Fill all fields").show();
            return false;
        }
        return true;
    }

    public void clearBtnOnAction(ActionEvent event) {
        clear();
    }

    public void clear() {
        txtPatientName.clear();
        txtNurseName.clear();
        txtDoctorName.clear();
        txtDiagnosedDate.setValue(null);
        cmbPatientType.setValue(null);
        txtDescription.clear();
    }

    public void setDiagnosisData(String patientName, String nurseName, LocalDate date, String doctorName,
                                 String patientType, String description, LocalDate diagnosedDate) {
        txtPatientName.setText(patientName);
        txtNurseName.setText(nurseName);
        txtDate.setValue(date);
        txtDoctorName.setText(doctorName);
        cmbPatientType.setValue(patientType);
        txtDescription.setText(description);
        txtDiagnosedDate.setValue(diagnosedDate);
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("DiagnosisManagementForm", context);
    }

    public void viewBtnOnAction(ActionEvent event) throws IOException {
        setUi("DiagnosesViewAndManageAllForm", context);
    }

    public void AddRecentBtnOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        AppointmentDto appointmentDto = getCurrentAppointment();
        if (appointmentDto != null) {
            txtPatientName.setText(patientBo.getPatientName(appointmentDto.getPatientName()));
            txtDoctorName.setText(doctorBo.getDoctorName(appointmentDto.getDoctorName()));
        } else {
            new Alert(Alert.AlertType.WARNING, "No recent patient data available.").show();
        }
    }
}
