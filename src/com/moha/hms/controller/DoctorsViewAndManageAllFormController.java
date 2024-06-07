package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.DepartmentBo;
import com.moha.hms.bo.custom.DoctorBo;
import com.moha.hms.bo.custom.UserRoleBo;
import com.moha.hms.dto.DepartmentDto;
import com.moha.hms.dto.DoctorDto;
import com.moha.hms.dto.UserRoleDto;
import com.moha.hms.view.tm.DoctorTm;
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

public class DoctorsViewAndManageAllFormController extends SuperBaseController {

    public BorderPane context;
    public ImageView backBtnImg;
    public ImageView addBtnImg;
    public TableView<DoctorTm> tblDoctors;
    public TableColumn<DoctorTm, String> colCount;
    public TableColumn<DoctorTm, String> colId;
    public TableColumn<DoctorTm, String> colName;
    public TableColumn<DoctorTm, String> colEmail;
    public TableColumn<DoctorTm, String> colSpecialist;
    public TableColumn<DoctorTm, String> colContact;
    public TableColumn<DoctorTm, String> colDepartmentName;
    public TableColumn<DoctorTm, Button> colUpdate;
    public TableColumn<DoctorTm, Button> colDelete;
    public TextField txtSearch;

    private final DoctorBo doctorBo = BoFactory.getInstance().getBo(BoFactory.BoType.DOCTOR);
    public Button btnBack;
    public Button btnAddNew;
    DepartmentBo departmentBo = BoFactory.getInstance().getBo(BoFactory.BoType.DEPARTMENT);
    private final ObservableList<DoctorTm> doctorTmObservableList = FXCollections.observableArrayList();
    private DoctorDto doctorDtoOuter;
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
        if (role.equals("DOCTOR")||role.equals("NURSE")||role.equals("RECEPTIONIST")){
            btnAddNew.setVisible(false);
            btnBack.setVisible(false);
        }
    }

    public void setTableColumns(){
        colCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colSpecialist.setCellValueFactory(new PropertyValueFactory<>("specialistFiled"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        colDepartmentName.setCellValueFactory(new PropertyValueFactory<>("departmentName"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("updateBtn"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("deleteBtn"));
    }

    public void loadAllData(String searchText) throws SQLException, ClassNotFoundException {
        int count = 0;
        doctorTmObservableList.clear();
        for (DoctorDto doctorDto : (searchText.length() > 0) ? doctorBo.searchDoctor(searchText) : doctorBo.loadAllDoctors()) {
            doctorDtoOuter = doctorDto;
            ImageView updateBtnImage = createImageView("com/moha/hms/assets/icons/editing.png");
            ImageView deleteBtnImage = createImageView("com/moha/hms/assets/icons/trash.png");

            Button updateBtn = createButton(updateBtnImage);
            Button deleteBtn = createButton(deleteBtnImage);
            count++;
            DoctorTm doctorTm = new DoctorTm(
                    String.valueOf(count), doctorDto.getId(), doctorDto.getName(), doctorDto.getEmail(),
                    doctorDto.getSpecialistFiled(), String.valueOf(doctorDto.getContactNumber()),
                    departmentBo.getDepartmentName(doctorDto.getDepartmentName()), updateBtn, deleteBtn
            );
            doctorTmObservableList.add(doctorTm);

            deleteBtn.setOnAction(event -> handleDeleteAction(doctorDto));

            updateBtn.setOnAction(event -> handleUpdateAction(doctorDto));
        }
        tblDoctors.setItems(doctorTmObservableList);

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

    private void handleDeleteAction(DoctorDto doctorDto) {
        try {
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Are you sure to DELETE this doctor?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get().equals(ButtonType.YES)) {
                if (doctorBo.deleteDoctor(doctorDto.getId())) {
                    new Alert(Alert.AlertType.INFORMATION, "Doctor deleted!").show();
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

    private void handleUpdateAction(DoctorDto doctorDto) {
        try {
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Do you want to UPDATE this doctor?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get().equals(ButtonType.YES)) {
                setUiWithData("DoctorAddNewForm", context, doctorDto);
            }
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
        }
    }

    public void setUiWithData(String location, BorderPane context, DoctorDto doctorDto) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/" + location + ".fxml"));
        Parent root = loader.load();
        SuperBaseController controller = loader.getController();

        if (controller instanceof DoctorAddNewFormController) {
            DoctorAddNewFormController doctorAddNewFormController = (DoctorAddNewFormController) controller;
            doctorAddNewFormController.saveBtn.setText("Update");
            doctorAddNewFormController.saveBtn.setOnAction(event -> {
                if (doctorAddNewFormController.preventNullValueOfSaveDoctor()) {
                    try {
                        String department = (String) doctorAddNewFormController.cmbDepartmentName.getValue();
                        Optional<DepartmentDto> selectedDepartmentDto =
                                doctorAddNewFormController.departmentDtoList.stream().filter(e -> e.getName().equals(department)).findFirst();
                        boolean doctor = doctorBo.updateDoctor(new DoctorDto(
                                doctorDto.getId(), doctorAddNewFormController.txtName.getText(),
                                doctorAddNewFormController.txtEmail.getText(),
                                doctorAddNewFormController.cmbSpecialist.getValue(),
                                doctorAddNewFormController.txtContactNo.getText(),
                                selectedDepartmentDto.get().getId()
                        ));

                        if (doctor) {
                            new Alert(Alert.AlertType.INFORMATION, "Doctor updated!").show();
                            doctorAddNewFormController.clear();
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
                    }
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try again").show();
                }
            });
            String departmentName = departmentBo.getDepartmentName(doctorDto.getDepartmentName());
            doctorAddNewFormController.setDoctorData(
                    doctorDto.getName(), doctorDto.getContactNumber(), doctorDto.getEmail(),
                    departmentName, doctorDto.getSpecialistFiled()
            );
        }
        context.getChildren().clear();
        context.setCenter(root);
    }

    public void addNewBtnOnAction(ActionEvent event) throws IOException {
        setUi("DoctorAddNewForm", context);
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("DoctorManagementForm", context);
    }
}
