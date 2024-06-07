package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.UserBo;
import com.moha.hms.bo.custom.UserRoleBo;
import com.moha.hms.dto.UserDto;
import com.moha.hms.dto.UserRoleDto;
import com.moha.hms.view.tm.UserTm;
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

public class UsersViewAndManageAllFormController extends SuperBaseController {
    public BorderPane context;
    public ImageView addBtnImg;
    public ImageView backBtnImg;
    public TableView tblUsers;
    public TableColumn colCount;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colEmail;
    public TableColumn colRole;
    public TableColumn colUpdate;
    public TableColumn colDelete;
    public TextField txtSearch;
    private String searchText = "";

    UserBo userBo = BoFactory.getInstance().getBo(BoFactory.BoType.USER);
    UserRoleBo userRoleBo = BoFactory.getInstance().getBo(BoFactory.BoType.USER_ROLE);
    ObservableList<UserTm> userTmObservableList = FXCollections.observableArrayList();
    UserDto userDtoOuter;


    public void initialize() throws SQLException, ClassNotFoundException {
        setTableColumns();
        loadAllUsers(searchText);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText = newValue;
            try {
                loadAllUsers(searchText);
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void setTableColumns() {
        colCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("updateBtn"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("deleteBtn"));
    }

    public void loadAllUsers(String searchText) throws SQLException, ClassNotFoundException {
        int count = 0;
        userTmObservableList.clear();
        for (UserDto userDto : (searchText.length()>0) ? userBo.searchUser(searchText) : userBo.loadAllUsers()
        ) {
            userDtoOuter = userDto;
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
            UserTm userTm = new UserTm(
                    String.valueOf(count),userDto.getId(), userDto.getEmail(), userDto.getName(),
                    userDto.getPassword(), userRoleBo.getUserRoleName(userDto.getRole()), updateBtn, deleteBtn
            );
            userTmObservableList.add(userTm);

            deleteBtn.setOnAction(event -> {
                try {
                    Alert alert = new Alert(
                            Alert.AlertType.CONFIRMATION,
                            "Are you sure to DELETE this user ???", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get().equals(ButtonType.YES)) {

                        if (userBo.deleteUser(userDto.getId())) {
                            new Alert(Alert.AlertType.INFORMATION, "User deleted !!!").show();
                            loadAllUsers(searchText);
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
                            "Do you want to UPDATE this user ???", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get().equals(ButtonType.YES)) {
                        setUiWithData("UserAddNewForm", context, userDto);
                    }
                } catch (IOException | SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
                }
            });
        }
        tblUsers.setItems(userTmObservableList);
    }

    public void setUiWithData(String location, BorderPane context, UserDto userDto) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/" + location + ".fxml"));
        Parent root = loader.load();
        SuperBaseController controller = loader.getController();

        if (controller instanceof UserAddNewFormController) {
            UserAddNewFormController userAddNewFormController = (UserAddNewFormController) controller;
            userAddNewFormController.saveBtn.setText("Update");
            userAddNewFormController.saveBtn.setOnAction(event -> {
                if (userAddNewFormController.preventNullValueOfSaveUser()) {
                    try {
                        String userRole = (String) userAddNewFormController.cmbUserRole.getValue();
                        Optional<UserRoleDto> selectedUserRoleDto =
                                userAddNewFormController.userRoleDtoList.stream().filter(e -> e.getRoleName().equals(userRole)).findFirst();
                        boolean user = userBo.updateUser(new UserDto(
                                userDto.getId(),userAddNewFormController.txtEmail.getText(),
                                userAddNewFormController.txtName.getText(),
                                null,
                                selectedUserRoleDto.get().getId()
                        ));

                        if (user) {
                            new Alert(Alert.AlertType.INFORMATION, userAddNewFormController.txtName.getText() +"User is Updated").show();
                            userAddNewFormController.clear();
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.WARNING,e.getMessage()).show();
                    }
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try again").show();
                }
            });
            String userRoleName = userRoleBo.getUserRoleName(userDto.getRole());
            userAddNewFormController.setUserData(
                    userDto.getName(),userRoleName,userDto.getEmail()
            );
        }
        context.getChildren().clear();
        context.setCenter(root);
    }

    public void addNewBtnOnAction(ActionEvent event) throws IOException {
        setUi("UserAddNewForm",context);
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("UserManagementForm",context);
    }
}
