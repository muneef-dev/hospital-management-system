package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.DepartmentBo;
import com.moha.hms.dto.DepartmentDto;
import com.moha.hms.view.tm.DepartmentTm;
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

public class DepartmentsViewAndManageAllFormController extends SuperBaseController {
    public BorderPane context;
    public TextField txtSearch;
    public TableView tblDepartments;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colFloor;
    public TableColumn colManager;
    public TableColumn colUpdate;
    public TableColumn colDelete;
    public TableColumn colCount;
    public Button btnAddNew;
    public Button btnBack;

    DepartmentBo departmentBo = BoFactory.getInstance().getBo(BoFactory.BoType.DEPARTMENT);
    ObservableList<DepartmentTm> departmentTmObservableList = FXCollections.observableArrayList();
    DepartmentDto departmentDtoOuter;

    private String searchText = "";

    public void initialize() throws SQLException, ClassNotFoundException {
        setTableColumns();
        loadAllDepartments(searchText);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText = newValue;
            try {
                loadAllDepartments(searchText);
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        String role = getUserRoleName();
        if (role.equals("DOCTOR")||role.equals("NURSE")){
            btnAddNew.setVisible(false);
            btnBack.setVisible(false);
        }
    }

    private void setTableColumns() {
        colCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colFloor.setCellValueFactory(new PropertyValueFactory<>("floor"));
        colManager.setCellValueFactory(new PropertyValueFactory<>("managerName"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("updateBtn"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("deleteBtn"));
    }

    public void loadAllDepartments(String searchText) throws SQLException, ClassNotFoundException {
        int count = 0;
        departmentTmObservableList.clear();
        for (DepartmentDto departmentDto : (searchText.length()>0) ? departmentBo.searchDepartment(searchText) : departmentBo.loadAllDepartments()
        ) {
            departmentDtoOuter = departmentDto;
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
            DepartmentTm departmentTm = new DepartmentTm(
                    String.valueOf(count),departmentDto.getId(), departmentDto.getName(), departmentDto.getFloor(),
                    departmentDto.getManagerName(), updateBtn, deleteBtn
            );
            departmentTmObservableList.add(departmentTm);

            deleteBtn.setOnAction(event -> {
                try {
                    Alert alert = new Alert(
                            Alert.AlertType.CONFIRMATION,
                            "Are you sure to DELETE this department ???", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get().equals(ButtonType.YES)) {

                        if (departmentBo.deleteDepartment(departmentDto.getId())) {
                            new Alert(Alert.AlertType.INFORMATION, "Department deleted !!!").show();
                            loadAllDepartments(searchText);
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
                            "Do you want to UPDATE this department ???", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get().equals(ButtonType.YES)) {
                        setUiWithData("DepartmentAddNewForm", context, departmentDto);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
                }
            });
        }
        tblDepartments.setItems(departmentTmObservableList);

        String userRoleName = getUserRoleName();
        if (userRoleName.equals("NURSE")){
            colUpdate.setVisible(false);
            colDelete.setVisible(false);
        } else if (userRoleName.equals("DOCTOR")) {
            colUpdate.setVisible(false);
            colDelete.setVisible(false);
        }
    }

    public void setUiWithData(String location, BorderPane context, DepartmentDto departmentDto) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/" + location + ".fxml"));
        Parent root = loader.load();
        SuperBaseController controller = loader.getController();

        if (controller instanceof DepartmentAddNewFormController) {
            DepartmentAddNewFormController departmentAddNewFormController = (DepartmentAddNewFormController) controller;
            departmentAddNewFormController.saveBtn.setText("Update");
            departmentAddNewFormController.saveBtn.setOnAction(event -> {
                if (departmentAddNewFormController.preventNullValueOfSaveDepartment()) {
                    try {
                        boolean department = departmentBo.updateDepartment(new DepartmentDto(
                                departmentDto.getId(),departmentAddNewFormController.txtName.getText(),
                                Integer.parseInt(departmentAddNewFormController.txtFloor.getText()),
                                String.valueOf(departmentAddNewFormController.cmbManager.getValue())
                        ));

                        if (department) {
                            new Alert(Alert.AlertType.INFORMATION, departmentAddNewFormController.txtName.getText() +"Department is Updated").show();
                            departmentAddNewFormController.clear();
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.WARNING,e.getMessage()).show();
                    }
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try again").show();
                }
            });
            departmentAddNewFormController.setDepartmentData(
                    departmentDto.getName(),departmentDto.getManagerName(), String.valueOf(departmentDto.getFloor())
            );
        }
        context.getChildren().clear();
        context.setCenter(root);
    }

    public void addNewBtnOnAction(ActionEvent event) throws IOException {
        setUi("DepartmentAddNewForm",context);
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("DepartmentManagementForm",context);
    }
}
