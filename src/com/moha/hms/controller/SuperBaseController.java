package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.UserBo;
import com.moha.hms.bo.custom.UserRoleBo;
import com.moha.hms.dto.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SuperBaseController {

    UserBo userBo = BoFactory.getInstance().getBo(BoFactory.BoType.USER);
    UserRoleBo userRoleBo = BoFactory.getInstance().getBo(BoFactory.BoType.USER_ROLE);

    private static UserDto currentUser;
    private static PatientDto currentPatient;
    private static AppointmentDto currentAppointment;
    private static AdmissionDto currentAdmission;
    private static DiagnosisDto currentDiagnosis;

    public static UserDto getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(UserDto user) {
        currentUser = user;
    }

    public String getUserRoleName() throws SQLException, ClassNotFoundException {
        return userRoleBo.getUserRoleName(currentUser.getRole());
    }
    public static PatientDto getCurrentPatient() {
        return currentPatient;
    }

    public static void setCurrentPatient(PatientDto currentPatient) {
        SuperBaseController.currentPatient = currentPatient;
    }
    public static DiagnosisDto getCurrentDiagnosis() {
        return currentDiagnosis;
    }

    public static void setCurrentDiagnosis(DiagnosisDto currentDiagnosis) {
        SuperBaseController.currentDiagnosis = currentDiagnosis;
    }

    public static AppointmentDto getCurrentAppointment() {
        return currentAppointment;
    }

    public static void setCurrentAppointment(AppointmentDto currentAppointment) {
        SuperBaseController.currentAppointment = currentAppointment;
    }

    public static AdmissionDto getCurrentAdmission() {
        return currentAdmission;
    }

    public static void setCurrentAdmission(AdmissionDto currentAdmission) {
        SuperBaseController.currentAdmission = currentAdmission;
    }

    public ImageView createImageView(String imagePath) {
        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        return imageView;
    }

    public Button createButton(ImageView imageView) {
        Button button = new Button("", imageView);
        button.getStyleClass().add("image-button");
        return button;
    }

    public boolean isNumeric(String str) {
        return str.matches("\\d+");
    }

    public boolean isValidPhoneNumber(String phoneNumber) {
        String phonePattern = "^\\d{10}$";
        Pattern pattern = Pattern.compile(phonePattern);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void setUi(String location, BorderPane context) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/" + location + ".fxml"));
        Node node = loader.load();
        context.getChildren().clear();
        context.setCenter(node);
    }

    public void setMainUi(String location, BorderPane mainContext) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/" + location + ".fxml"));
        Scene scene = new Scene(parent);
        scene.getStylesheets().add(getClass().getResource("../css/style.css").toExternalForm());
        Stage stage = (Stage) mainContext.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
    }
}

