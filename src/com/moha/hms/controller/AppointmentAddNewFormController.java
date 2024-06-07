package com.moha.hms.controller;

import com.jfoenix.controls.JFXTimePicker;
import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.AppointmentBo;
import com.moha.hms.bo.custom.DoctorBo;
import com.moha.hms.bo.custom.PatientBo;
import com.moha.hms.bo.custom.UserBo;
import com.moha.hms.dto.AppointmentDto;
import com.moha.hms.dto.DoctorDto;
import com.moha.hms.dto.PatientDto;
import com.moha.hms.dto.UserRoleDto;
import com.moha.hms.util.KeyGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class AppointmentAddNewFormController extends SuperBaseController {
    public BorderPane context;
    public TextField txtPatientName;
    public TextField txtReason;
    public ComboBox<String> cmbStatus;
    public TextField txtDoctorName;
    public DatePicker txtDate;
    public Button saveBtn;
    public JFXTimePicker timePicker;

    AppointmentBo appointmentBo = BoFactory.getInstance().getBo(BoFactory.BoType.APPOINTMENT);
    PatientBo patientBo = BoFactory.getInstance().getBo(BoFactory.BoType.PATIENT);
    DoctorBo doctorBo = BoFactory.getInstance().getBo(BoFactory.BoType.DOCTOR);
    UserBo userBo = BoFactory.getInstance().getBo(BoFactory.BoType.USER);
    ObservableList<String> observableListStatus = FXCollections.observableArrayList();
    AutoCompletionBinding<String> autoCompletionBindingDoctor;
    AutoCompletionBinding<String> autoCompletionBindingPatient;
    String[] suggestionDoctorNames = new String[0];
    String[] suggestionPatientNames = new String[0];
    Set<String> suggestionDoctorNamesSet = new HashSet<>();
    Set<String> suggestionPatientNamesSet = new HashSet<>();
    List<PatientDto> patientDtoList = new ArrayList<>();
    List<DoctorDto> doctorDtoList = new ArrayList<>();

    public void initialize() {
        try {
            loadAllGenders();
            loadAllDoctors();
            loadAllPatients();
            TextFields.bindAutoCompletion(txtDoctorName, suggestionDoctorNamesSet);
            TextFields.bindAutoCompletion(txtPatientName, suggestionPatientNamesSet);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadAllDoctors() throws SQLException, ClassNotFoundException {
        ArrayList<String> doctorNamesList = new ArrayList<>();
        doctorDtoList = doctorBo.loadAllDoctors();
        for (DoctorDto doctorDto: doctorDtoList) {
            doctorNamesList.add(doctorDto.getName());
        }
        suggestionDoctorNames = doctorNamesList.toArray(new String[0]);
        suggestionDoctorNamesSet = new HashSet<>(Arrays.asList(suggestionDoctorNames));
    }

    public void loadAllPatients() throws SQLException, ClassNotFoundException {
        ArrayList<String> patientNamesList = new ArrayList<>();
        patientDtoList = patientBo.loadAllPatients();
        for (PatientDto patientDto: patientDtoList) {
            patientNamesList.add(patientDto.getName());
        }
        suggestionPatientNames = patientNamesList.toArray(new String[0]);
        suggestionPatientNamesSet = new HashSet<>(patientNamesList);
    }

    public void loadAllGenders() {
        observableListStatus.clear();
        observableListStatus.addAll("ACTIVE", "DISCHARGE");
        cmbStatus.setItems(observableListStatus);
        cmbStatus.setValue("ACTIVE");
    }

    public void saveBtnOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (preventNullValueOfSaveAppointment()) {
            String patientNameText = txtPatientName.getText();
            String doctorNameText = txtDoctorName.getText(); // Fix this line to get the doctor name
            Optional<PatientDto> selectedPatientDto =
                    patientDtoList.stream().filter(e -> e.getName().equals(patientNameText)).findFirst();
            Optional<DoctorDto> selectedDoctorDto =
                    doctorDtoList.stream().filter(e -> e.getName().equals(doctorNameText)).findFirst();

            if (selectedPatientDto.isPresent() && selectedDoctorDto.isPresent()) {
                LocalTime localTime = timePicker.getValue();
                LocalDate localDate = txtDate.getValue();
                Date sqlDate = Date.valueOf(localDate);
                LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
                java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(localDateTime);

                List<AppointmentDto> existingAppointments = appointmentBo.getAppointmentsByDoctorAndDate(
                        Integer.parseInt(selectedDoctorDto.get().getId()), sqlDate);

                boolean isDoubleBooked = existingAppointments.stream().anyMatch(appointment -> appointment.getTime().equals(timestamp));

                if (isDoubleBooked) {
                    new Alert(Alert.AlertType.ERROR, "The doctor is already booked for this time. Please choose another time.").show();
                    return;
                }

                AppointmentDto appointmentDto = new AppointmentDto(
                        KeyGenerator.generateId(), sqlDate, timestamp,
                        selectedPatientDto.get().getId(), selectedDoctorDto.get().getId(),
                        txtReason.getText(),
                        String.valueOf(cmbStatus.getValue()), SuperBaseController.getCurrentUser().getId()
                );
                boolean appointment = appointmentBo.createAppointment(appointmentDto);

                if (appointment) {
                    SuperBaseController.setCurrentAppointment(appointmentDto);
                    new Alert(Alert.AlertType.INFORMATION, "Appointment is Saved").show();
                    clear();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Selected patient or doctor not found.").show();
            }
        }
    }


    public boolean preventNullValueOfSaveAppointment() {
        if (txtPatientName.getText().isEmpty() || txtReason.getText().isEmpty() ||
                cmbStatus.getValue() == null || txtDoctorName.getText().isEmpty() || txtDate.getValue() == null) {
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
        txtReason.clear();
        cmbStatus.setValue(null);
        txtDoctorName.clear();
        txtDate.setValue(null);
    }

    public void setAppointmentData(String patientName, String reason, String status, LocalDate date, String doctorName, Time time) {
        txtPatientName.setText(patientName);
        txtReason.setText(reason);
        cmbStatus.setValue(status);
        txtDate.setValue(date);
        txtDoctorName.setText(doctorName);

        LocalTime localTime = time.toLocalTime();
        timePicker.setValue(localTime);
    }

    public void viewBtnOnAction(ActionEvent event) throws IOException {
        setUi("AppointmentsViewAndManageAllForm", context);
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("AppointmentManagementForm", context);
    }

    public void AddRecentBtnOnAction(ActionEvent event) {
        PatientDto patientDto = getCurrentPatient();
        if (patientDto != null) {
            txtPatientName.setText(patientDto.getName());
        } else {
            new Alert(Alert.AlertType.WARNING, "No recent patient data available.").show();
        }
    }
}
