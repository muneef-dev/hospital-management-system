package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.UserBo;
import com.moha.hms.bo.custom.UserRoleBo;
import com.moha.hms.dto.UserDto;
import com.moha.hms.dto.UserRoleDto;
import com.moha.hms.util.KeyGenerator;
import com.moha.hms.util.SendMailManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class UserAddNewFormController extends SuperBaseController {
    public BorderPane context;
    public ImageView clearBtnImg;
    public ImageView viewBtnImg;
    public ImageView backBtnImg;
    public ImageView saveBtnImg;
    public TextField txtName;
    public TextField txtEmail;
    public ComboBox cmbUserRole;
    @FXML
    public Button saveBtn;

    UserBo userBo = BoFactory.getInstance().getBo(BoFactory.BoType.USER);
    UserRoleBo userRoleBo = BoFactory.getInstance().getBo(BoFactory.BoType.USER_ROLE);
    ObservableList<String> observableListRoles = FXCollections.observableArrayList();
    List<UserRoleDto> userRoleDtoList = new ArrayList<>();

    public void initialize() throws SQLException, ClassNotFoundException {
        loadAllRoles();
    }

    public void clearBtnOnAction(ActionEvent event) {
        txtName.clear();
        txtEmail.clear();
        cmbUserRole.getItems().clear();
    }

    public void loadAllRoles() throws SQLException, ClassNotFoundException {
        observableListRoles.clear();
        userRoleDtoList = userRoleBo.loadAllUserRoles();
        for (UserRoleDto dto: userRoleDtoList) {
            observableListRoles.add(dto.getRoleName());
        }
        cmbUserRole.setItems(observableListRoles);
        cmbUserRole.setValue(observableListRoles.get(0));
    }

    public void saveBtnOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String userName = txtName.getText();
        String userEmail = txtEmail.getText();
        String password = KeyGenerator.generateId();
        String userRole = (String) cmbUserRole.getValue();

        Optional<UserRoleDto> selectedUserRoleDto =
                userRoleDtoList.stream().filter(e -> e.getRoleName().equals(userRole)).findFirst();
        if (preventNullValueOfSaveUser()) {
            boolean user = userBo.createUser(new UserDto(
                    KeyGenerator.generateId(), txtEmail.getText(),
                    txtName.getText(), password, selectedUserRoleDto.get().getId()
            ));

            if (user){
                SendMailManager.sendMail(generateMailContent(userName, userEmail, password , userRole));
                new Alert(Alert.AlertType.INFORMATION, "User is Saved").show();
                clear();
            }
        }
    }

    public List<String> generateMailContent(String userName, String userEmail, String password, String userRole) {
        String subject = "Welcome to the Hospital - Your Account Details";
        String text = String.format(
                "Dear %s,\n\n" +
                        "Welcome to the Hospital! We are delighted to have you on board.\n\n" +
                        "Here are your account details:\n" +
                        "Email: %s\n" +
                        "Role: %s\n" +
                        "Password: %s\n\n" +
                        "Please ensure to keep this password secure and do not share it with anyone.\n\n" +
                        "Best regards,\n[Your Hospital Management System Team]",
                userName, userEmail, userRole, password
        );
        String alertMessage = "Password has been sent to the given user email address";

        List<String> mailContent = new ArrayList<>();
        mailContent.add(subject);
        mailContent.add(text);
        mailContent.add(userEmail);
        mailContent.add(alertMessage);

        return mailContent;
    }


    public boolean preventNullValueOfSaveUser() {
        if (txtName.getText().isEmpty() || txtEmail.getText().isEmpty() || cmbUserRole.getValue() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Fill the all fields").show();
            return false;
        }
        if (!isValidEmail(txtEmail.getText())) {
            new Alert(Alert.AlertType.WARNING, "Enter a VALID EMAIL ADDRESS (e.g., moha@example.com)").show();
            return false;
        }
        return true;
    }

    public void setUserData(String name, String role, String email) {
        txtName.setText(name);
        cmbUserRole.setValue(role);
        txtEmail.setText(email);
    }

    public void clear() {
        txtName.clear();
        txtEmail.clear();
        cmbUserRole.setValue(null);
    }

    public void addRoleBtnOnAction(ActionEvent event) throws IOException {
        setUi("UserRoleAddNewForm", context);
    }

    public void viewBtnOnAction(ActionEvent event) throws IOException {
        setUi("UsersViewAndManageAllForm", context);
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("UserManagementForm", context);
    }
}
