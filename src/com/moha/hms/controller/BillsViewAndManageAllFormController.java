package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.BillBo;
import com.moha.hms.bo.custom.DoctorBo;
import com.moha.hms.bo.custom.PatientBo;
import com.moha.hms.bo.custom.UserBo;
import com.moha.hms.dto.BillDto;
import com.moha.hms.view.tm.BillTm;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class BillsViewAndManageAllFormController extends SuperBaseController {
    public BorderPane context;
    public TextField txtSearch;
    public TableView<BillTm> tblBill;
    public TableColumn<BillTm, String> colCount;
    public TableColumn<BillTm, String> colId;
    public TableColumn<BillTm, String> colPatientName;
    public TableColumn<BillTm, String> colIssueDate;
    public TableColumn<BillTm, String> colPaymentStatus;
    public TableColumn<BillTm, Double> colDoctorFee;
    public TableColumn<BillTm, Double> colMedicationCost;
    public TableColumn<BillTm, Double> colLabTestCost;
    public TableColumn<BillTm, Double> colOtherCost;
    public TableColumn<BillTm, String> colBilledUser;
    public TableColumn<BillTm, Button> colUpdate;
    public TableColumn<BillTm, Button> colDelete;

    private final BillBo billBo = BoFactory.getInstance().getBo(BoFactory.BoType.BILL);
    public Button btnAddNew;
    public Button btnBack;
    PatientBo patientBo = BoFactory.getInstance().getBo(BoFactory.BoType.PATIENT);
    UserBo userBo = BoFactory.getInstance().getBo(BoFactory.BoType.USER);
    private final ObservableList<BillTm> billTmObservableList = FXCollections.observableArrayList();
    private BillDto billDtoOuter;
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
    }

    private void setTableColumns() {
        colCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colIssueDate.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        colPaymentStatus.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        colDoctorFee.setCellValueFactory(new PropertyValueFactory<>("doctorFee"));
        colMedicationCost.setCellValueFactory(new PropertyValueFactory<>("medicationCost"));
        colLabTestCost.setCellValueFactory(new PropertyValueFactory<>("labTestCost"));
        colOtherCost.setCellValueFactory(new PropertyValueFactory<>("otherServicesCost"));
        colBilledUser.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("updateBtn"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("deleteBtn"));
    }

    public void loadAllData(String searchText) throws SQLException, ClassNotFoundException {
        int count = 0;
        billTmObservableList.clear();
        for (BillDto billDto : (searchText.length() > 0) ? billBo.searchBill(searchText) : billBo.loadAllBills()) {
            billDtoOuter = billDto;
            ImageView updateBtnImage = createImageView("com/moha/hms/assets/icons/editing.png");
            ImageView deleteBtnImage = createImageView("com/moha/hms/assets/icons/trash.png");

            Button updateBtn = createButton(updateBtnImage);
            Button deleteBtn = createButton(deleteBtnImage);
            count++;
            BillTm billTm = new BillTm(
                    String.valueOf(count), billDto.getId(), billDto.getQrCode(),
                    billDto.getIssueDate(), billDto.getPaymentStatus(), billDto.getDoctorFee(),
                    billDto.getMedicationCost(), billDto.getLabTestCost(), billDto.getOtherServicesCost(),
                    patientBo.getPatientName(billDto.getPatientName()), billDto.getMedicationId(),
                    userBo.getUserName(billDto.getUserId()), updateBtn, deleteBtn
            );
            billTmObservableList.add(billTm);

            deleteBtn.setOnAction(event -> handleDeleteAction(billDto));

            updateBtn.setOnAction(event -> handleUpdateAction(billDto));
        }
        tblBill.setItems(billTmObservableList);

        String userRoleName = getUserRoleName();
        if (userRoleName.equals("NURSE")){
            colUpdate.setVisible(false);
            colDelete.setVisible(false);
        } else if (userRoleName.equals("DOCTOR")) {
            colDelete.setVisible(false);
        }  else if (userRoleName.equals("RECEPTIONIST")) {
            colDelete.setVisible(false);
        }
    }

    private void handleDeleteAction(BillDto billDto) {
        try {
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Are you sure to DELETE this bill?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get().equals(ButtonType.YES)) {
                if (billBo.deleteBill(billDto.getId())) {
                    new Alert(Alert.AlertType.INFORMATION, "Bill deleted!").show();
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

    private void handleUpdateAction(BillDto billDto) {
        try {
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Do you want to UPDATE this bill?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get().equals(ButtonType.YES)) {
                setUiWithData("BillAddNewForm", context, billDto);
            }
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
        }
    }

    public void setUiWithData(String location, BorderPane context, BillDto billDto) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/" + location + ".fxml"));
        Parent root = loader.load();
        SuperBaseController controller = loader.getController();

        if (controller instanceof BillAddNewFormController) {
            BillAddNewFormController billAddNewFormController = (BillAddNewFormController) controller;
            billAddNewFormController.saveBtn.setText("Update");
            billAddNewFormController.saveBtn.setOnAction(event -> {
                if (billAddNewFormController.preventNullValueOfSaveBill()) {
                    try {
                        boolean billUpdated = billBo.updateBill(new BillDto(
                                billDto.getId(), billDto.getQrCode(), billDto.getIssueDate(),
                                billAddNewFormController.cmbPaymentStatus.getValue(),
                                Double.parseDouble(billAddNewFormController.txtDoctorFee.getText()),
                                Double.parseDouble(billAddNewFormController.txtMedicationCost.getText()),
                                Double.parseDouble(billAddNewFormController.txtLabTestCost.getText()),
                                Double.parseDouble(billAddNewFormController.txtOtherCost.getText()),
                                billAddNewFormController.txtPatientName.getText(), billDto.getMedicationId(),
                                billDto.getUserId()
                        ));

                        if (billUpdated) {
                            new Alert(Alert.AlertType.INFORMATION, "Bill is Updated").show();
                            billAddNewFormController.clear();
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
                    }
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try again").show();
                }
            });
            billAddNewFormController.setBillData(
                    billDto.getPatientName(),billDto.getPaymentStatus(), LocalDate.parse(billDto.getIssueDate().toString()),
                    String.valueOf(billDto.getDoctorFee().intValue()), String.valueOf(billDto.getMedicationCost().intValue()),
                    String.valueOf(billDto.getLabTestCost().intValue()), String.valueOf(billDto.getOtherServicesCost().intValue()),
                    billDto.getQrCode()
            );
        }
        context.getChildren().clear();
        context.setCenter(root);
    }

//    public ImageView createImageView(String path) {
//        ImageView imageView = new ImageView(new Image(path));
//        imageView.setFitWidth(15);
//        imageView.setFitHeight(15);
//        return imageView;
//    }

//    private Button createButton(ImageView imageView) {
//        return new Button("", imageView);
//    }

    public void addNewBtnOnAction(ActionEvent event) throws IOException {
        setUi("BillAddNewForm", context);
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("BillManagementForm", context);
    }
}
