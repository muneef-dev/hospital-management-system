package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.UserBo;
import com.moha.hms.dto.UserDto;
import com.moha.hms.util.KeyGenerator;
import com.moha.hms.util.PasswordManager;
import com.moha.hms.util.SendMailManager;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsChangePasswordFormController extends SuperBaseController {
    public BorderPane context;
    public TextField txtNewPassword;
    public TextField txtReEnterNewPassword;
    public TextField txtOTP;
    public Button btnContinue;
    public Button btnSendOtp;

    UserBo userBo = BoFactory.getInstance().getBo(BoFactory.BoType.USER);

    String textOTP = "";
    String newPassword = "";
    String reEnterNewPassword = "";

    String generatedOTP = KeyGenerator.generateId();

    public void initialize() {
        enableFields(true);
    }

    public void btnContinueOnAction(ActionEvent event) {
        textOTP = txtOTP.getText();
        newPassword = txtNewPassword.getText();
        reEnterNewPassword = txtReEnterNewPassword.getText();

        // Validation
        if (textOTP.isEmpty() || newPassword.isEmpty() || reEnterNewPassword.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "All fields are required.").show();
            return;
        }

        if (!generatedOTP.equals(txtOTP.getText().trim())) {
            new Alert(Alert.AlertType.WARNING, "One time passwords (OTP) do not match.").show();
            return;
        }

        // Check password strength
        String missingCriteria = PasswordManager.getPasswordCriteria(newPassword);
        if (!missingCriteria.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Password does not meet security requirements. Missing criteria: " + missingCriteria).show();
            return;
        }

        if (!newPassword.equals(reEnterNewPassword)) {
            new Alert(Alert.AlertType.WARNING, "New passwords do not match.").show();
            return;
        }

        UserDto currentUser = getCurrentUser();
        try {
            if (currentUser != null) {
                // Update user's password
                userBo.updateUser(new UserDto(
                        currentUser.getId(), currentUser.getEmail(), currentUser.getName(),
                        newPassword, currentUser.getRole()
                ));
                new Alert(Alert.AlertType.INFORMATION, "Password changed successfully.").show();
                clearFields();
            } else {
                new Alert(Alert.AlertType.WARNING, "Current password is incorrect.").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
        }
    }

    public List<String> generatePasswordResetMailContent(String userName, String userEmail, String otp) {
        String subject = "Password Reset OTP";
        String text = String.format(
                "Dear %s,\n\n" +
                        "A One Time Password (OTP) has been generated for resetting your password.\n\n" +
                        "Here is your OTP:\n" +
                        "%s\n\n" +
                        "Please use this OTP to reset your password.\n\n" +
                        "If you did not request this password reset, please ignore this email.\n\n" +
                        "Best regards,\n[Your Hospital Management System Team]"
                , userName, otp
        );
        String alertMessage = "An OTP has been sent to your email. Please check your inbox.";

        List<String> mailContent = new ArrayList<>();
        mailContent.add(subject);
        mailContent.add(text);
        mailContent.add(userEmail);
        mailContent.add(alertMessage);

        return mailContent;
    }

    public void clearBtnOnAction(ActionEvent event) {
        clearFields();
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("SettingsForm", context);
    }

    private void clearFields() {
        txtOTP.clear();
        txtNewPassword.clear();
        txtReEnterNewPassword.clear();
    }

    public void btnSendOTPOnAction(ActionEvent event) {
        btnSendOtp.setDisable(true);
        SendMailManager.sendMail(generatePasswordResetMailContent(
                getCurrentUser().getName(), getCurrentUser().getEmail(), generatedOTP));
        enableFields(false);
    }

    public void enableFields(Boolean bool){
        txtNewPassword.setDisable(bool);
        txtReEnterNewPassword.setDisable(bool);
        btnContinue.setDisable(bool);
    }
}
