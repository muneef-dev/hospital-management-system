package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.DepartmentBo;
import com.moha.hms.bo.custom.RoomBo;
import com.moha.hms.dto.DepartmentDto;
import com.moha.hms.dto.RoomDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomAddNewFormController extends SuperBaseController {
    public BorderPane context;
    public TextField txtRoomName;
    public ComboBox<String> cmbAvailability;
    public TextField txtBedCount;
    public ComboBox<String> cmbDepartmentName;
    public Button saveBtn;

    RoomBo roomBo = BoFactory.getInstance().getBo(BoFactory.BoType.ROOM);
    DepartmentBo departmentBo = BoFactory.getInstance().getBo(BoFactory.BoType.DEPARTMENT);
    ObservableList<String> observableListDepartments = FXCollections.observableArrayList();
    ObservableList<String> observableListAvailability = FXCollections.observableArrayList();
    List<DepartmentDto> departmentDtoList = new ArrayList<>();

    public void initialize() {
        try {
            loadAllDepartments();
            loadAllAvailabilitys();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadAllAvailabilitys() {
        observableListAvailability.clear();
        observableListAvailability.addAll("TRUE", "FALSE");
        cmbAvailability.setItems(observableListAvailability);
        cmbAvailability.setValue("TRUE");
    }

    public void loadAllDepartments() throws SQLException, ClassNotFoundException {
        observableListDepartments.clear();
        departmentDtoList = departmentBo.loadAllDepartments();
        for (DepartmentDto dto: departmentDtoList) {
            observableListDepartments.add(dto.getName());
        }
        cmbDepartmentName.setItems(observableListDepartments);
    }

    public void saveBtnOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String department = cmbDepartmentName.getValue();
        Optional<DepartmentDto> selectedDepartmentDto =
                departmentDtoList.stream().filter(e -> e.getName().equals(department)).findFirst();
        if (preventNullValueOfSaveRoom()) {
            boolean room = roomBo.createRoom(new RoomDto(
                    setRoomId(), txtRoomName.getText(), Integer.parseInt(txtBedCount.getText()),
                    String.valueOf(cmbAvailability.getValue()), selectedDepartmentDto.get().getId()
            ));

            if (room) {
                new Alert(Alert.AlertType.INFORMATION, "Room is Saved").show();
                clear();
            }
        }
    }

    public String setRoomId() throws SQLException, ClassNotFoundException {
        String lastRoomId = roomBo.getLastRoomId();
        if (lastRoomId!=null){
            String[] splitData = lastRoomId.split("-");
            String lastIntegerNumberAsString = splitData[1];
            int lastIntegerIdAsInt = Integer.parseInt(lastIntegerNumberAsString);
            lastIntegerIdAsInt++;
            return "R-"+lastIntegerIdAsInt;
        }else {
            return "R-1";
        }
    }

    public boolean preventNullValueOfSaveRoom() {
        if (txtRoomName.getText().isEmpty() || cmbAvailability.getValue() == null ||
                txtBedCount.getText().isEmpty() || cmbDepartmentName.getValue() == null) {
            new Alert(Alert.AlertType.INFORMATION, "Fill all the fields").show();
            return false;
        }

        if (!isNumeric(txtBedCount.getText())) {
            new Alert(Alert.AlertType.WARNING, "Enter NUMERIC VALUES for Bed Count").show();
            return false;
        }
        return true;
    }

    public void clearBtnOnAction(ActionEvent event) {
        clear();
    }

    public void clear() {
        txtRoomName.clear();
        cmbAvailability.setValue(null);
        txtBedCount.clear();
        cmbDepartmentName.setValue(null);
    }

    public void setRoomData(String roomName, String availability, String bedCount, String departmentName) {
        txtRoomName.setText(roomName);
        cmbAvailability.setValue(availability);
        txtBedCount.setText(bedCount);
        cmbDepartmentName.setValue(departmentName);
    }

    public void viewBtnOnAction(ActionEvent event) throws IOException {
        setUi("RoomsViewAndManageAllForm", context);
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("RoomManagementForm", context);
    }
}
