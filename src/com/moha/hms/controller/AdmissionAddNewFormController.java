package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.*;
import com.moha.hms.dto.*;
import com.moha.hms.util.KeyGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class AdmissionAddNewFormController extends SuperBaseController {
    public BorderPane context;
    public TextField txtPatientName;
    public DatePicker txtDischargeDate;
    public TextField txtDoctorName;
    public DatePicker txtAdmittingDate;
    public ComboBox<String> cmbRoomName;
    public Button saveBtn;

    AdmissionBo admissionBo = BoFactory.getInstance().getBo(BoFactory.BoType.ADMISSION);
    DiagnosisBo diagnosisBo = BoFactory.getInstance().getBo(BoFactory.BoType.DIAGNOSIS);
    PatientBo patientBo = BoFactory.getInstance().getBo(BoFactory.BoType.PATIENT);
    RoomBo roomBo = BoFactory.getInstance().getBo(BoFactory.BoType.ROOM);
    DoctorBo doctorBo = BoFactory.getInstance().getBo(BoFactory.BoType.DOCTOR);
    ObservableList<String> observableListRooms = FXCollections.observableArrayList();
    List<RoomDto> roomDtoList = new ArrayList<>();
    Set<String> suggestionDoctorNamesSet = new HashSet<>();
    Set<String> suggestionPatientNamesSet = new HashSet<>();


    public void initialize() {
        try {
            loadAllRooms();
            loadAllDoctors();
            loadAllPatients();
            TextFields.bindAutoCompletion(txtDoctorName, suggestionDoctorNamesSet);
            TextFields.bindAutoCompletion(txtPatientName, suggestionPatientNamesSet);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadAllRooms() throws SQLException, ClassNotFoundException {
        observableListRooms.clear();
        roomDtoList = roomBo.loadAllRooms();
        for (RoomDto roomDto : roomDtoList) {
            observableListRooms.add(roomDto.getName());
        }
        cmbRoomName.setItems(observableListRooms);
    }

    public void loadAllDoctors() throws SQLException, ClassNotFoundException {
        ArrayList<String> doctorNamesList = new ArrayList<>();
        for (DoctorDto doctorDto : doctorBo.loadAllDoctors()) {
            doctorNamesList.add(doctorDto.getName());
        }
        suggestionDoctorNamesSet = new HashSet<>(doctorNamesList);
    }

    public void loadAllPatients() throws SQLException, ClassNotFoundException {
        ArrayList<String> patientNamesList = new ArrayList<>();
        for (PatientDto patientDto : patientBo.loadAllPatients()) {
            patientNamesList.add(patientDto.getName());
        }
        suggestionPatientNamesSet = new HashSet<>(patientNamesList);
    }

    public void saveBtnOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (preventNullValueOfSaveAdmission()) {
            List<PatientDto> patientDtoList = patientBo.loadAllPatients();
            List<DoctorDto> doctorDtoList = doctorBo.loadAllDoctors();

            String patientNameText = txtPatientName.getText();
            String doctorNameText = txtDoctorName.getText(); // Fix this line to get the doctor name
            String roomNameText = cmbRoomName.getValue();
            Optional<PatientDto> selectedPatientDto =
                    patientDtoList.stream().filter(e -> e.getName().equals(patientNameText)).findFirst();
            Optional<DoctorDto> selectedDoctorDto =
                    doctorDtoList.stream().filter(e -> e.getName().equals(doctorNameText)).findFirst();
            Optional<RoomDto> selectedRoomDto =
                    roomDtoList.stream().filter(e -> e.getName().equals(roomNameText)).findFirst();


            List<DiagnosisDto> diagnosisDtoList = diagnosisBo.diagnosedPatient(selectedPatientDto.get().getId());
            List<String> diagnosisIds = diagnosisDtoList.stream()
                    .map(DiagnosisDto::getId) // Extracting the id property
                    .collect(Collectors.toList());

            // Join the diagnosis IDs into a single string without brackets
            String diagnosisIdsString = String.join(",", diagnosisIds);

            AdmissionDto admissionDto = new AdmissionDto(
                    KeyGenerator.generateId(), Date.valueOf(txtAdmittingDate.getValue()),
                    Date.valueOf(txtDischargeDate.getValue()), selectedPatientDto.get().getId(),
                    selectedRoomDto.get().getId(), selectedDoctorDto.get().getId(),
                    diagnosisIdsString
            );
            System.out.println(admissionDto);
            boolean admission = admissionBo.createAdmission(admissionDto);

            if (admission) {
                SuperBaseController.setCurrentAdmission(admissionDto);
                new Alert(Alert.AlertType.INFORMATION, "Admission is Saved").show();
                clear();
            }
        }
    }


    public boolean preventNullValueOfSaveAdmission() {
        if (txtPatientName.getText().isEmpty() || cmbRoomName.getValue() == null ||
                txtAdmittingDate.getValue() == null || txtDischargeDate.getValue() == null ||
                txtDoctorName.getText().isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION, "Fill all the fields").show();
            return false;
        }
        return true;
    }

    public void clearBtnOnAction(ActionEvent event) {
        clear();
    }

    public void clear() {
        txtPatientName.clear();
        txtDoctorName.clear();
        cmbRoomName.setValue(null);
        txtAdmittingDate.setValue(null);
        txtDischargeDate.setValue(null);
    }

    public void setAdmissionData(String patientName, String roomName, LocalDate dischargeDate,
                                 String doctorName, LocalDate admittingDate) {
        txtPatientName.setText(patientName);
        cmbRoomName.setValue(roomName);
        txtDischargeDate.setValue(dischargeDate);
        txtDoctorName.setText(doctorName);
        txtAdmittingDate.setValue(admittingDate);
    }

    public void viewBtnOnAction(ActionEvent event) throws IOException {
        setUi("AdmissionsViewAndManageAllForm", context);
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("AdmissionManagementForm", context);
    }

    public void AddRecentBtnOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        PatientDto patientDto = getCurrentPatient();
        AppointmentDto appointmentDto = getCurrentAppointment();

//        if (patientDto != null) {
//            txtPatientName.setText(patientDto.getName());
            if (appointmentDto != null) {
                txtDoctorName.setText(doctorBo.getDoctorName(appointmentDto.getDoctorName()));
                txtPatientName.setText(patientBo.getPatientName(appointmentDto.getPatientName()));
            } else {
                new Alert(Alert.AlertType.WARNING, "No recent appointment data available.").show();
            }
//        } else {
//            new Alert(Alert.AlertType.WARNING, "No recent patient data available.").show();
//        }
    }
}
