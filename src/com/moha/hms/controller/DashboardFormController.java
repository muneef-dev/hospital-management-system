package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.AppointmentBo;
import com.moha.hms.bo.custom.BillBo;
import com.moha.hms.bo.custom.InventoryItemBo;
import com.moha.hms.bo.custom.PatientBo;
import com.moha.hms.dto.InventoryItemDto;
import com.moha.hms.dto.UserDto;
import com.moha.hms.entity.Appointment;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class DashboardFormController extends SuperBaseController {
    public BorderPane mainContext;
    public BorderPane context;
    public TextField txtSearch;
    public ImageView lblProfileImage;
    public Text lblName;
    public Text lblEmail;
    public Button btnPatient;
    public Button btnDoctor;
    public Button btnNurse;
    public Button btnAppointment;
    public Button btnAdmission;
    public Button btnDiagnosis;
    public Button btnInventory;
    public Button btnDepartment;
    public Button btnRoom;
    public Button btnBill;
    public Button btnReports;
    public Button btnSystemUser;
    public Text txtNumberOfNotification;
    public Text txtNumberOfPatients;
    public Text txtNumberOfAppointments;
    public VBox vBoxLeftSide;
    public Text lblHelloName;
    public Text lblRoleName;
    public Button btnMedication;
    public Text txtTime;
    public Text lblDate;
    public Tab btnLowerQuantity;
    public Label lblProductExpare;
    public Tab btnExpireSoon;
    public Label lblProductQuantity;
    public BarChart barChart;


    private final PatientBo patientBo = BoFactory.getInstance().getBo(BoFactory.BoType.PATIENT);
    private final InventoryItemBo inventoryItemBo = BoFactory.getInstance().getBo(BoFactory.BoType.INVENTORY_ITEM);
    private final AppointmentBo appointmentBo = BoFactory.getInstance().getBo(BoFactory.BoType.APPOINTMENT);
    private final BillBo billBo = BoFactory.getInstance().getBo(BoFactory.BoType.BILL);

    public void initialize() {
        try {
            setPatientCount();
            setTime();
            setCurrentUser();
            preventAccess();
            loadLowStockItems();
            setAppointmentCount();
            populateBarChart();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void populateBarChart() throws SQLException, ClassNotFoundException {
        double totalDoctorFee = billBo.getTotalAmountOfColumns("doctor_fee");
        double totalMedicationCost = billBo.getTotalAmountOfColumns("medication_cost");
        double totalLabTestCost = billBo.getTotalAmountOfColumns("labtest_cost");
        double totalOtherServicesCost = billBo.getTotalAmountOfColumns("other_services_cost");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Doctor Fee", totalDoctorFee));
        series.getData().add(new XYChart.Data<>("Medication", totalMedicationCost));
        series.getData().add(new XYChart.Data<>("Lab Test", totalLabTestCost));
        series.getData().add(new XYChart.Data<>("Other Services", totalOtherServicesCost));

        barChart.getData().add(series);
    }

    public void setPatientCount() {
        try {
            int patientCount = patientBo.getPatientCount();
            txtNumberOfPatients.setText(String.valueOf(patientCount));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            txtNumberOfPatients.setText("Error");
        }
    }

    public void setAppointmentCount() {
        try {
            int appointmentCount = appointmentBo.getAppointmentCount();
            txtNumberOfAppointments.setText(String.valueOf(appointmentCount));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            txtNumberOfAppointments.setText("Error");
        }
    }

    public void preventAccess() throws SQLException, ClassNotFoundException {
        String userRoleName = getUserRoleName();

        switch (userRoleName) {
            case "DOCTOR":
                disableButtons(btnInventory, btnBill, btnReports, btnSystemUser);
                break;
            case "NURSE":
                disableButtons(btnDiagnosis, btnBill, btnReports, btnSystemUser);
                break;
            case "RECEPTIONIST":
                disableButtons(btnNurse, btnAdmission, btnDiagnosis,
                        btnInventory, btnDepartment, btnRoom, btnReports, btnSystemUser,btnMedication);
                break;
            case "MANAGER":
                disableButtons(btnSystemUser);
                break;
            default:
                break;
        }
    }

    private void disableButtons(Button... buttons) {
        for (Button button : buttons) {
            button.setDisable(true);
        }
    }

    private void setTime(){
        lblDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        e -> {
                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss");
                            txtTime.setText(LocalTime.now().format(dateTimeFormatter));
                        }
                ),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void btnDashboardOnAction(ActionEvent event) throws IOException {
        setMainUi("DashboardForm", context);
    }

    public void btnPatientOnAction(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        String role = getUserRoleName();
        if (role.equals("ADMIN")||role.equals("MANAGER")||role.equals("RECEPTIONIST")){
            setUi("PatientManagementForm", context);
        } else {
            setUi("PatientsViewAndManageAllForm", context);
        }
    }

    public void btnDoctorOnAction(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        String role = getUserRoleName();
        if (role.equals("ADMIN")||role.equals("MANAGER")){
            setUi("DoctorManagementForm", context);
        } else {
            setUi("DoctorsViewAndManageAllForm", context);
        }
    }

    public void btnNurseOnAction(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        String role = getUserRoleName();
        if (role.equals("ADMIN")||role.equals("MANAGER")){
            setUi("NurseManagementForm", context);
        } else {
            setUi("NursesViewAndManageAllForm", context);
        }
    }

    public void btnAppointmentOnAction(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        String role = getUserRoleName();
        if (role.equals("ADMIN")||role.equals("MANAGER")||role.equals("RECEPTIONIST")){
            setUi("AppointmentManagementForm", context);
        } else {
            setUi("AppointmentsViewAndManageAllForm", context);
        }
    }

    public void btnAdmissionOnAction(ActionEvent event) throws IOException {
        setUi("AdmissionManagementForm", context);
    }

    public void btnInventoryOnAction(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        String role = getUserRoleName();
        if (role.equals("ADMIN")||role.equals("MANAGER")||role.equals("NURSE")){
            setUi("InventoryItemManagementForm", context);
        }
    }

    public void btnDepartmentOnAction(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        String role = getUserRoleName();
        if (role.equals("ADMIN")||role.equals("MANAGER")){
            setUi("DepartmentManagementForm", context);
        } else {
            setUi("DepartmentViewAndManageAllForm", context);
        }
    }

    public void btnRoomOnAction(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        String role = getUserRoleName();
        if (role.equals("ADMIN")||role.equals("MANAGER")){
            setUi("RoomManagementForm", context);
        } else {
            setUi("RoomsViewAndManageAllForm", context);
        }
    }

    public void btnMedicationOnAction(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        String role = getUserRoleName();
        if (role.equals("ADMIN")||role.equals("MANAGER")||role.equals("NURSE")||role.equals("DOCTOR")){
            setUi("MedicationManagementForm", context);
        }
    }

    public void btnBillOnAction(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        String role = getUserRoleName();
        if (role.equals("ADMIN")||role.equals("MANAGER")||role.equals("RECEPTIONIST")){
            setUi("BillManagementForm", context);
        }
    }

    public void btnDiagnosisOnAction(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        String role = getUserRoleName();
        if (role.equals("ADMIN")||role.equals("MANAGER")||role.equals("DOCTOR")){
            setUi("DiagnosisManagementForm", context);
        }
    }

    public void btnReportOnAction(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        String role = getUserRoleName();
        if (role.equals("ADMIN")||role.equals("MANAGER")){
            setUi("ReportManagementForm",context);
        }
    }

    public void btnSystemUserOnAction(ActionEvent event) throws IOException {
        setUi("UserManagementForm", context);
    }

    public void btnSettingsOnAction(ActionEvent event) throws IOException {
        setUi("SettingsForm", context);
    }

    public void setCurrentUser() throws SQLException, ClassNotFoundException {
        UserDto currentUser = getCurrentUser();
        if (currentUser != null) {
            lblName.setText(currentUser.getName());
            lblEmail.setText(currentUser.getEmail());
            lblHelloName.setText(currentUser.getName());
        }
    }

    public void btnLogoutOnAction(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/LoginForm.fxml"));
        Scene scene = new Scene(parent);
        scene.getStylesheets().add(getClass().getResource("../css/style.css").toExternalForm());
        Stage stage = (Stage) mainContext.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    private void loadLowStockItems() throws SQLException, ClassNotFoundException {
        List<InventoryItemDto> lowStockItems = inventoryItemBo.getItemsWithLowStock();
        StringBuilder lowStockDisplay = new StringBuilder();

        for (InventoryItemDto item : lowStockItems) {
            lowStockDisplay.append(item.getName()).append(" - ").append(item.getQtyOnHand()).append("\n");
        }

        lblProductQuantity.setText(lowStockDisplay.toString());
    }

    public void btnExpiringSoonOnAction(ActionEvent event) {
    }

    public void btnLowerQuantityOnAction(ActionEvent event) {
    }

    public void btnOnMouseClickedNotification(MouseEvent mouseEvent) {
    }

}


