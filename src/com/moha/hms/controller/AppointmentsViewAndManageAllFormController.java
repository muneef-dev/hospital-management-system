package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.*;
import com.moha.hms.dto.AppointmentDto;
import com.moha.hms.util.KeyGenerator;
import com.moha.hms.view.tm.AppointmentTm;
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
import java.sql.Time;
import java.util.Optional;

public class AppointmentsViewAndManageAllFormController extends SuperBaseController {

    public BorderPane context;
    public TextField txtSearch;
    public TableView<AppointmentTm> tblAppointments;
    public TableColumn<AppointmentTm, String> colCount;
    public TableColumn<AppointmentTm, String> colId;
    public TableColumn<AppointmentTm, String> colReason;
    public TableColumn<AppointmentTm, String> colUserName;
    public TableColumn<AppointmentTm, String> colPatientName;
    public TableColumn<AppointmentTm, String> colDate;
    public TableColumn<AppointmentTm, String> colTime;
    public TableColumn<AppointmentTm, String> colStatus;
    public TableColumn<AppointmentTm, String> colDoctorName;
    public TableColumn<AppointmentTm, Button> colUpdate;
    public TableColumn<AppointmentTm, Button> colDelete;
    public Button btnAddNew;
    public Button btnBack;

    AppointmentBo appointmentBo = BoFactory.getInstance().getBo(BoFactory.BoType.APPOINTMENT);
    PatientBo patientBo = BoFactory.getInstance().getBo(BoFactory.BoType.PATIENT);
    DoctorBo doctorBo = BoFactory.getInstance().getBo(BoFactory.BoType.DOCTOR);
    UserBo userBo = BoFactory.getInstance().getBo(BoFactory.BoType.USER);
    ObservableList<AppointmentTm> appointmentTmObservableList = FXCollections.observableArrayList();

    public void initialize() throws SQLException, ClassNotFoundException {
        setTableColumns();
        loadAllAppointments();

        String role = getUserRoleName();
        if (role.equals("DOCTOR")||role.equals("NURSE")){
            btnAddNew.setVisible(false);
            btnBack.setVisible(false);
        }
    }

    private void setTableColumns() {
        colCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colReason.setCellValueFactory(new PropertyValueFactory<>("reason"));
        colPatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colDoctorName.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("updateBtn"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("deleteBtn"));
    }

    private void loadAllAppointments() throws SQLException, ClassNotFoundException {
        int count = 0;
        for (AppointmentDto appointmentDto : appointmentBo.loadAllAppointments()) {
            count++;
            Button updateBtn = createButton(createImageView("com/moha/hms/assets/icons/editing.png"));
            Button deleteBtn = createButton(createImageView("com/moha/hms/assets/icons/trash.png"));

            AppointmentTm appointmentTm = new AppointmentTm(
                    String.valueOf(count), appointmentDto.getId(), appointmentDto.getDate(), appointmentDto.getTime(),
                    patientBo.getPatientName(appointmentDto.getPatientName()),
                    doctorBo.getDoctorName(appointmentDto.getDoctorName()), appointmentDto.getReason(),
                    appointmentDto.getStatus(), userBo.getUserName(appointmentDto.getUserName()),updateBtn, deleteBtn
            );

            deleteBtn.setOnAction(event -> handleDeleteAction(appointmentDto));
            updateBtn.setOnAction(event -> handleUpdateAction(appointmentDto));

            appointmentTmObservableList.add(appointmentTm);
        }
        tblAppointments.setItems(appointmentTmObservableList);

        String userRoleName = getUserRoleName();
        if (userRoleName.equals("NURSE")){
            colUpdate.setVisible(false);
            colDelete.setVisible(false);
        } else if (userRoleName.equals("DOCTOR")) {
            colUpdate.setVisible(false);
            colDelete.setVisible(false);
        }  else if (userRoleName.equals("RECEPTIONIST")) {
            colDelete.setVisible(false);
        }
    }

    private void handleDeleteAction(AppointmentDto appointmentDto) {
        try {
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to DELETE this appointment?",
                    ButtonType.YES,
                    ButtonType.NO
            );

            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.isPresent() && buttonType.get() == ButtonType.YES) {
                AppointmentBo appointmentBo = BoFactory.getInstance().getBo(BoFactory.BoType.APPOINTMENT);
                if (appointmentBo.deleteAppointment(appointmentDto.getId())) {
                    loadAllAppointments();
                    new Alert(Alert.AlertType.INFORMATION, "Appointment deleted successfully!").show();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to delete appointment!").show();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred: " + e.getMessage()).show();
        }
    }

    private void handleUpdateAction(AppointmentDto appointmentDto) {
        try {
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Do you want to UPDATE this appointment ???", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get().equals(ButtonType.YES)) {
                setUiWithData("AppointmentAddNewForm", context, appointmentDto);
            }
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
        }
    }

    private void setUiWithData(String location, BorderPane context, AppointmentDto appointmentDto) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/" + location + ".fxml"));
        Parent root = loader.load();
        SuperBaseController controller = loader.getController();

        if (controller instanceof AppointmentAddNewFormController) {
            AppointmentAddNewFormController appointmentAddNewFormController = (AppointmentAddNewFormController) controller;
            appointmentAddNewFormController.saveBtn.setText("Update");
            appointmentAddNewFormController.saveBtn.setOnAction(event -> {
                if (appointmentAddNewFormController.preventNullValueOfSaveAppointment()) {
                    try {
                        boolean patient = appointmentBo.updateAppointment(new AppointmentDto(
                                appointmentDto.getId(), Date.valueOf(appointmentAddNewFormController.txtDate.getValue()),
                                java.sql.Timestamp.valueOf(String.valueOf(appointmentAddNewFormController.timePicker.getValue())),
                                appointmentAddNewFormController.txtPatientName.getText(),
                                appointmentAddNewFormController.txtDoctorName.getText(), appointmentAddNewFormController.txtReason.getText(),
                                appointmentAddNewFormController.cmbStatus.getValue(), "user"
                        ));

                        if (patient) {
                            new Alert(Alert.AlertType.INFORMATION, "Patient is Updated").show();
                            appointmentAddNewFormController.clear();
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.WARNING,e.getMessage()).show();
                    }
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try again").show();
                }
            });
            appointmentAddNewFormController.setAppointmentData(
                    appointmentDto.getPatientName(), appointmentDto.getReason(), appointmentDto.getStatus(),
                    Date.valueOf(String.valueOf(appointmentDto.getDate())).toLocalDate(), appointmentDto.getDoctorName(),
                    (Time) appointmentDto.getTime()
            );
        }
        context.getChildren().clear();
        context.setCenter(root);
    }

    public void addNewBtnOnAction(ActionEvent event) throws IOException {
        setUi("AppointmentAddNewForm", context);
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("AppointmentManagementForm", context);
    }
}
