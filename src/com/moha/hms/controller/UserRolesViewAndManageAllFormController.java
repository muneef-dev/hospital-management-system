package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.UserRoleBo;
import com.moha.hms.dto.UserRoleDto;
import com.moha.hms.view.tm.UserRoleTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class UserRolesViewAndManageAllFormController extends SuperBaseController {
    public BorderPane context;
    public TextField txtSearch;
    public TableView<UserRoleTm> tblUserRoles;
    public TableColumn colCount;
    public TableColumn colId;
    public TableColumn colRoleDescription;
    public TableColumn colRoleName;
    public TableColumn colUpdate;
    public TableColumn colDelete;

    private String searchText = "";

    UserRoleBo userRoleBo = BoFactory.getInstance().getBo(BoFactory.BoType.USER_ROLE);
    ObservableList<UserRoleTm> userRoleTmObservableList = FXCollections.observableArrayList();
    UserRoleDto userRoleDtoOuter;


    public void initialize() throws SQLException, ClassNotFoundException {
        setTableColumns();
        loadAllUserRoles(searchText);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText = newValue;
            try {
                loadAllUserRoles(searchText);
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void setTableColumns() {
        colCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRoleDescription.setCellValueFactory(new PropertyValueFactory<>("roleDescription"));
        colRoleName.setCellValueFactory(new PropertyValueFactory<>("roleName"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("updateBtn"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("deleteBtn"));
    }

    public void loadAllUserRoles(String searchText) throws SQLException, ClassNotFoundException {
        int count = 0;
        userRoleTmObservableList.clear();
        for (UserRoleDto userRoleDto : (searchText.length()>0) ? userRoleBo.searchUserRole(searchText) : userRoleBo.loadAllUserRoles()
        ) {
            userRoleDtoOuter = userRoleDto;
            // Create ImageViews for the buttons
            ImageView updateBtnImage = new ImageView(new Image("com/moha/hms/assets/icons/editing.png"));
            ImageView deleteBtnImage = new ImageView(new Image("com/moha/hms/assets/icons/trash.png"));

            // Set the size of the images if needed
            updateBtnImage.setFitWidth(15);
            updateBtnImage.setFitHeight(15);
            deleteBtnImage.setFitWidth(15);
            deleteBtnImage.setFitHeight(15);

            Button updateBtn = new Button("", updateBtnImage);
            Button deleteBtn = new Button("", deleteBtnImage);
            count++;
            UserRoleTm userRoleTm = new UserRoleTm(
                    String.valueOf(count),userRoleDto.getId(), userRoleDto.getRoleDescription(),
                    userRoleDto.getRoleName(), updateBtn, deleteBtn
            );
            userRoleTmObservableList.add(userRoleTm);

            deleteBtn.setOnAction(event -> {
                try {
                    Alert alert = new Alert(
                            Alert.AlertType.CONFIRMATION,
                            "Are you sure to DELETE this userRole ???", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get().equals(ButtonType.YES)) {

                        if (userRoleBo.deleteUserRole(userRoleDto.getId())) {
                            new Alert(Alert.AlertType.INFORMATION, "UserRole deleted !!!").show();
                            loadAllUserRoles(searchText);
                        } else {
                            new Alert(Alert.AlertType.WARNING, "Try again").show();
                        }
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
                }
            });

            updateBtn.setOnAction(event -> {
                try {
                    Alert alert = new Alert(
                            Alert.AlertType.CONFIRMATION,
                            "Do you want to UPDATE this userRole ???", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get().equals(ButtonType.YES)) {
                        setUiWithData("UserRoleAddNewForm", context, userRoleDto);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
                }
            });
        }
        tblUserRoles.setItems(userRoleTmObservableList);
    }

    public void setUiWithData(String location, BorderPane context, UserRoleDto userRoleDto) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/" + location + ".fxml"));
        Parent root = loader.load();
        SuperBaseController controller = loader.getController();

        if (controller instanceof UserRoleAddNewFormController) {
            UserRoleAddNewFormController userRoleAddNewFormController = (UserRoleAddNewFormController) controller;
            userRoleAddNewFormController.saveBtn.setText("Update");
            userRoleAddNewFormController.saveBtn.setOnAction(event -> {
                if (userRoleAddNewFormController.preventNullValueOfSaveUserRole()) {
                    try {
                        boolean userRole = userRoleBo.updateUserRole(new UserRoleDto(
                                userRoleDto.getId(),userRoleAddNewFormController.txtRoleDescription.getText(),
                                userRoleAddNewFormController.txtRoleName.getText()
                        ));

                        if (userRole) {
                            new Alert(Alert.AlertType.INFORMATION, userRoleAddNewFormController.txtRoleName.getText() +"UserRole is Updated").show();
                            userRoleAddNewFormController.clear();
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.WARNING,e.getMessage()).show();
                    }
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try again").show();
                }
            });
            userRoleAddNewFormController.setUserRoleData(
                    userRoleDto.getRoleDescription(),userRoleDto.getRoleName()
            );
        }
        context.getChildren().clear();
        context.setCenter(root);
    }

    public void addNewBtnOnAction(ActionEvent event) throws IOException {
        setUi("UserRoleAddNewForm",context);
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("UserAddNewForm",context);
    }
}
