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

public class LoginForgotPasswordFormController extends SuperBaseController {
    public BorderPane context;
    public TextField txtNewPassword;
    public TextField txtReEnterNewPassword;
    public TextField txtOTP;
    public Button btnContinue;
    public Button btnSendOtp;
    public TextField txtEmail;
    private UserBo userBo;
    private String generatedOTP;
    private List<UserDto> currentUser;

    public void initialize() {
        userBo = BoFactory.getInstance().getBo(BoFactory.BoType.USER);
        generatedOTP = KeyGenerator.generateId();
        currentUser = new ArrayList<>();
        enableFields(true);
        txtOTP.setDisable(true);
    }

    public void btnContinueOnAction(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        String email = txtEmail.getText();
        String textOTP = txtOTP.getText();
        String newPassword = txtNewPassword.getText();
        String reEnterNewPassword = txtReEnterNewPassword.getText();

        if (email.isEmpty() || textOTP.isEmpty() || newPassword.isEmpty() || reEnterNewPassword.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "All fields are required.").show();
            return;
        }

        if (!generatedOTP.equals(textOTP.trim())) {
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

        currentUser = userBo.searchUserThroughEmail(email);

        if (currentUser != null && !currentUser.isEmpty()) {
            UserDto user = currentUser.get(0);
            userBo.updateUser(new UserDto(
                    user.getId(), user.getEmail(), user.getName(),
                    newPassword, user.getRole()
            ));
            new Alert(Alert.AlertType.INFORMATION, "Password changed successfully.").show();
            setUi("LoginForm",context);
        } else {
            new Alert(Alert.AlertType.WARNING, "User not found.").show();
        }
    }

    public void btnSendOTPOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String email = txtEmail.getText();
        currentUser = userBo.searchUserThroughEmail(email);


        if (!isValidEmail(txtEmail.getText())) {
            new Alert(Alert.AlertType.WARNING, "Enter a VALID EMAIL ADDRESS (e.g., moha@example.com)").show();
            return;
        }

        if (currentUser == null || currentUser.isEmpty()) {
            new Alert(Alert.AlertType.WARNING,
                    "User not found or incomplete user data or This email don't have an user account.").show();
            return;
        }

        btnSendOtp.setDisable(true);
        txtOTP.setDisable(false);

        UserDto user = currentUser.get(0);
        String userName = user.getName();
        String userEmail = user.getEmail();
        SendMailManager.sendMail(generatePasswordResetMailContent(userName, userEmail, generatedOTP));
        enableFields(false);
    }

    public void clearBtnOnAction(ActionEvent event) {
        clearFields();
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("LoginForm", context);
    }

    private void clearFields() {
        txtEmail.clear();
        txtOTP.clear();
        txtNewPassword.clear();
        txtReEnterNewPassword.clear();
    }

    private List<String> generatePasswordResetMailContent(String userName, String userEmail, String otp) {
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

    private void enableFields(Boolean bool) {
        txtNewPassword.setDisable(bool);
        txtReEnterNewPassword.setDisable(bool);
        btnContinue.setDisable(bool);
    }
}
