package com.moha.hms.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.UserBo;
import com.moha.hms.dto.UserDto;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class LoginFormController extends SuperBaseController {
    public BorderPane context;
    public JFXTextField txtEmail;
    public JFXPasswordField txtPassword;
    public Hyperlink hyperLinkForgotPassword;
    public HBox contextHBox;

    private UserBo userBo = BoFactory.getInstance().getBo(BoFactory.BoType.USER);

    public void loginBtnOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String email = txtEmail.getText().trim().toLowerCase();
        String password = txtPassword.getText();

        if (email.isEmpty() || password.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Email and Password fields cannot be empty").show();
            return;
        }

        if (!isValidEmail(txtEmail.getText())) {
            new Alert(Alert.AlertType.WARNING, "Enter a VALID EMAIL ADDRESS (e.g., moha@example.com)").show();
            return;
        }

        try {
            List<UserDto> userDtoList = userBo.searchUserThroughEmail(email);
            if (userDtoList != null && !userDtoList.isEmpty()) {
                Optional<UserDto> matchedUser = userBo.authenticateUser(email, password);
                if (matchedUser.isPresent()) {
                    SuperBaseController.setCurrentUser(matchedUser.get()); // Set the current user
                    loadDashboard();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Email or Password is incorrect").show();
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "User not found under the given EMAIL ID").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading dashboard: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }


    private void loadDashboard() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/DashboardForm.fxml"));
        Scene scene = new Scene(parent);

        scene.getStylesheets().add(getClass().getResource("../css/style.css").toExternalForm());
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public void forgotPasswordOnMouseClicked(MouseEvent mouseEvent) throws IOException {
        setUi("LoginForgotPasswordForm",context);
    }
}

