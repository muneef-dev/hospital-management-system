package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.DepartmentBo;
import com.moha.hms.bo.custom.RoomBo;
import com.moha.hms.dto.DepartmentDto;
import com.moha.hms.dto.RoomDto;
import com.moha.hms.view.tm.RoomTm;
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

public class RoomsViewAndManageAllFormController extends SuperBaseController {

    public BorderPane context;
    public TextField txtSearch;
    public TableView<RoomTm> tblRooms;
    public TableColumn<RoomTm, String> colCount;
    public TableColumn<RoomTm, String> colId;
    public TableColumn<RoomTm, String> colRoomName;
    public TableColumn<RoomTm, String> colBedCount;
    public TableColumn<RoomTm, String> colAvailability;
    public TableColumn<RoomTm, String> colDepartmentName;
    public TableColumn<RoomTm, Button> colUpdate;
    public TableColumn<RoomTm, Button> colDelete;

    private final RoomBo roomBo = BoFactory.getInstance().getBo(BoFactory.BoType.ROOM);
    private final DepartmentBo departmentBo = BoFactory.getInstance().getBo(BoFactory.BoType.DEPARTMENT);
    private final ObservableList<RoomTm> roomTmObservableList = FXCollections.observableArrayList();
    public Button btnBack;
    public Button btnAddNew;
    private String searchText = "";

    public void initialize() throws SQLException, ClassNotFoundException {
        setTableColumns();
        loadAllRooms(searchText);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText = newValue;
            try {
                loadAllRooms(searchText);
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
        colRoomName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colBedCount.setCellValueFactory(new PropertyValueFactory<>("bedCount"));
        colAvailability.setCellValueFactory(new PropertyValueFactory<>("availability"));
        colDepartmentName.setCellValueFactory(new PropertyValueFactory<>("departmentName"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("updateBtn"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("deleteBtn"));
    }

    public void loadAllRooms(String searchText) throws SQLException, ClassNotFoundException {
        int count = 0;
        roomTmObservableList.clear();
        for (RoomDto roomDto : (searchText.length() > 0) ? roomBo.searchRoom(searchText) : roomBo.loadAllRooms()) {
            ImageView updateBtnImage = createImageView("com/moha/hms/assets/icons/editing.png");
            ImageView deleteBtnImage = createImageView("com/moha/hms/assets/icons/trash.png");

            Button updateBtn = createButton(updateBtnImage);
            Button deleteBtn = createButton(deleteBtnImage);
            count++;
            RoomTm roomTm = new RoomTm(
                    String.valueOf(count), roomDto.getId(), roomDto.getName(), roomDto.getBedCount(),
                    roomDto.getAvailability(), departmentBo.getDepartmentName(roomDto.getDepartmentName()), updateBtn, deleteBtn
            );
            roomTmObservableList.add(roomTm);

            deleteBtn.setOnAction(event -> handleDeleteAction(roomDto));

            updateBtn.setOnAction(event -> handleUpdateAction(roomDto));
        }
        tblRooms.setItems(roomTmObservableList);

        String userRoleName = getUserRoleName();
        if (userRoleName.equals("NURSE")){
            colUpdate.setVisible(false);
            colDelete.setVisible(false);
        } else if (userRoleName.equals("DOCTOR")) {
            colUpdate.setVisible(false);
            colDelete.setVisible(false);
        }
    }

    private void handleDeleteAction(RoomDto roomDto) {
        try {
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Are you sure to DELETE this room?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.isPresent() && buttonType.get().equals(ButtonType.YES)) {
                if (roomBo.deleteRoom(roomDto.getId())) {
                    new Alert(Alert.AlertType.INFORMATION, "Room deleted!").show();
                    loadAllRooms(searchText);
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try again").show();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
        }
    }

    private void handleUpdateAction(RoomDto roomDto) {
        try {
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Do you want to UPDATE this room?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.isPresent() && buttonType.get().equals(ButtonType.YES)) {
                setUiWithData("RoomAddNewForm", context, roomDto);
            }
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
        }
    }

    public void setUiWithData(String location, BorderPane context, RoomDto roomDto) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/" + location + ".fxml"));
        Parent root = loader.load();
        SuperBaseController controller = loader.getController();

        if (controller instanceof RoomAddNewFormController) {
            RoomAddNewFormController roomAddNewFormController = (RoomAddNewFormController) controller;
            roomAddNewFormController.saveBtn.setText("Update");
            roomAddNewFormController.saveBtn.setOnAction(event -> {
                if (roomAddNewFormController.preventNullValueOfSaveRoom()) {
                    try {
                        String department = (String) roomAddNewFormController.cmbDepartmentName.getValue();
                        Optional<DepartmentDto> selectedDepartmentDto =
                                roomAddNewFormController.departmentDtoList.stream().filter(e -> e.getName().equals(department)).findFirst();
                        boolean room = roomBo.updateRoom(new RoomDto(
                                roomDto.getId(), roomAddNewFormController.txtRoomName.getText(),
                                Integer.parseInt(roomAddNewFormController.txtBedCount.getText()),
                                roomAddNewFormController.cmbAvailability.getValue(),
                                selectedDepartmentDto.get().getId()
                        ));

                        if (room) {
                            new Alert(Alert.AlertType.INFORMATION, "Room updated!").show();
                            roomAddNewFormController.clear();
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
                    }
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try again").show();
                }
            });
            String departmentName = departmentBo.getDepartmentName(roomDto.getDepartmentName());
            roomAddNewFormController.setRoomData(
                    roomDto.getName(), String.valueOf(roomDto.getAvailability()).toUpperCase(),
                    String.valueOf(roomDto.getBedCount()), departmentName
            );
        }
        context.getChildren().clear();
        context.setCenter(root);
    }

    public void addNewBtnOnAction(ActionEvent event) throws IOException {
        setUi("RoomAddNewForm", context);
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("RoomManagementForm", context);
    }
}
