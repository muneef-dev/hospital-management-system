package com.moha.hms;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.UserBo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Initializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        initializeData();

        Parent parent = FXMLLoader.load(getClass().getResource("view/LoginForm.fxml"));
        Scene scene = new Scene(parent);
        scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.centerOnScreen();
        stage.show();
    }

    private void initializeData(){
        UserBo userBo = BoFactory.getInstance().getBo(BoFactory.BoType.USER);
        userBo.initializeSystem();
    }
}
