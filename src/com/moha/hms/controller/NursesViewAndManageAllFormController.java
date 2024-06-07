package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.DepartmentBo;
import com.moha.hms.bo.custom.NurseBo;
import com.moha.hms.dto.DepartmentDto;
import com.moha.hms.dto.NurseDto;
import com.moha.hms.view.tm.NurseTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class NursesViewAndManageAllFormController extends SuperBaseController {

    public BorderPane context;
    public ImageView backBtnImg;
    public ImageView addBtnImg;
    public TableView<NurseTm> tblNurses;
    public TableColumn<NurseTm, String> colCount;
    public TableColumn<NurseTm, String> colId;
    public TableColumn<NurseTm, String> colName;
    public TableColumn<NurseTm, String> colEmail;
    public TableColumn<NurseTm, String> colContact;
    public TableColumn<NurseTm, String> colDepartmentName;
    public TableColumn<NurseTm, Button> colUpdate;
    public TableColumn<NurseTm, Button> colDelete;
    public TextField txtSearch;

    private final NurseBo nurseBo = BoFactory.getInstance().getBo(BoFactory.BoType.NURSE);
    private final DepartmentBo departmentBo = BoFactory.getInstance().getBo(BoFactory.BoType.DEPARTMENT);
    private final ObservableList<NurseTm> nurseTmObservableList = FXCollections.observableArrayList();
    public Button btnBack;
    public Button btnAddNew;
    private String searchText = "";

    public void initialize() throws SQLException, ClassNotFoundException {
        setTableColumns();
        loadAllData(searchText);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText = newValue;
            try {
                loadAllData(searchText);
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

    public void setTableColumns(){
        colCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        colDepartmentName.setCellValueFactory(new PropertyValueFactory<>("departmentName"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("updateBtn"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("deleteBtn"));
    }

    public void loadAllData(String searchText) throws SQLException, ClassNotFoundException {
        int count = 0;
        nurseTmObservableList.clear();
        for (NurseDto nurseDto : (searchText.length() > 0) ? nurseBo.searchNurse(searchText) : nurseBo.loadAllNurses()) {
            ImageView updateBtnImage = createImageView("com/moha/hms/assets/icons/editing.png");
            ImageView deleteBtnImage = createImageView("com/moha/hms/assets/icons/trash.png");

            Button updateBtn = createButton(updateBtnImage);
            Button deleteBtn = createButton(deleteBtnImage);
            count++;
            NurseTm nurseTm = new NurseTm(
                    String.valueOf(count), nurseDto.getId(), nurseDto.getName(), nurseDto.getEmail(),
                    nurseDto.getContactNumber(), departmentBo.getDepartmentName(nurseDto.getDepartmentName()), updateBtn, deleteBtn
            );
            nurseTmObservableList.add(nurseTm);

            deleteBtn.setOnAction(event -> handleDeleteAction(nurseDto));

            updateBtn.setOnAction(event -> handleUpdateAction(nurseDto));
        }
        tblNurses.setItems(nurseTmObservableList);

        String userRoleName = getUserRoleName();
        if (userRoleName.equals("NURSE")){
            colUpdate.setVisible(false);
            colDelete.setVisible(false);
        } else if (userRoleName.equals("DOCTOR")) {
            colUpdate.setVisible(false);
            colDelete.setVisible(false);
        }  else if (userRoleName.equals("RECEPTIONIST")) {
            colUpdate.setVisible(false);
            colDelete.setVisible(false);
        }
    }

    private void handleDeleteAction(NurseDto nurseDto) {
        try {
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Are you sure to DELETE this nurse?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.isPresent() && buttonType.get().equals(ButtonType.YES)) {
                if (nurseBo.deleteNurse(nurseDto.getId())) {
                    new Alert(Alert.AlertType.INFORMATION, "Nurse deleted!").show();
                    loadAllData(searchText);
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try again").show();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
        }
    }

    private void handleUpdateAction(NurseDto nurseDto) {
        try {
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Do you want to UPDATE this nurse?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.isPresent() && buttonType.get().equals(ButtonType.YES)) {
                setUiWithData("NurseAddNewForm", context, nurseDto);
            }
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
        }
    }

    public void setUiWithData(String location, BorderPane context, NurseDto nurseDto) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/" + location + ".fxml"));
        Parent root = loader.load();
        SuperBaseController controller = loader.getController();

        if (controller instanceof NurseAddNewFormController) {
            NurseAddNewFormController nurseAddNewFormController = (NurseAddNewFormController) controller;
            nurseAddNewFormController.saveBtn.setText("Update");
            nurseAddNewFormController.saveBtn.setOnAction(event -> {
                if (nurseAddNewFormController.preventNullValueOfSaveNurse()) {
                    try {
                        String department = (String) nurseAddNewFormController.cmbDepartmentName.getValue();
                        Optional<DepartmentDto> selectedDepartmentDto =
                                nurseAddNewFormController.departmentDtoList.stream().filter(e -> e.getName().equals(department)).findFirst();
                        boolean nurse = nurseBo.updateNurse(new NurseDto(
                                nurseDto.getId(), nurseAddNewFormController.txtName.getText(),
                                nurseAddNewFormController.txtEmail.getText(),
                                nurseAddNewFormController.txtContactNo.getText(),
                                selectedDepartmentDto.get().getId()
                        ));

                        if (nurse) {
                            new Alert(Alert.AlertType.INFORMATION, "Nurse updated!").show();
                            nurseAddNewFormController.clear();
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
                    }
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try again").show();
                }
            });
            String departmentName = departmentBo.getDepartmentName(nurseDto.getDepartmentName());
            nurseAddNewFormController.setNurseData(
                    nurseDto.getName(), nurseDto.getContactNumber(), nurseDto.getEmail(), departmentName
            );
        }
        context.getChildren().clear();
        context.setCenter(root);
    }

    public void addNewBtnOnAction(ActionEvent event) throws IOException {
        setUi("NurseAddNewForm", context);
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("NurseManagementForm", context);
    }
}
