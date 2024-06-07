package com.moha.hms.controller;

import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.InventoryItemBo;
import com.moha.hms.dto.InventoryItemDto;
import com.moha.hms.view.tm.InventoryItemTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class InventoryItemsViewAndManageAllFormController extends SuperBaseController {

    public BorderPane context;
    public TextField txtSearch;
    public TableView<InventoryItemTm> tblItem;
    public TableColumn<InventoryItemTm, String> colCount;
    public TableColumn<InventoryItemTm, String> colId;
    public TableColumn<InventoryItemTm, String> colItemName;
    public TableColumn<InventoryItemTm, String> colCategory;
    public TableColumn<InventoryItemTm, String> colSupplierName;
    public TableColumn<InventoryItemTm, String> colMinOrderQty;
    public TableColumn<InventoryItemTm, String> colQtyOnHand;
    public TableColumn<InventoryItemTm, String> colBuyingPrice;
    public TableColumn<InventoryItemTm, String> colSellingPrice;
    public TableColumn<InventoryItemTm, Button> colUpdate;
    public TableColumn<InventoryItemTm, Button> colDelete;

    private final InventoryItemBo inventoryItemBo = BoFactory.getInstance().getBo(BoFactory.BoType.INVENTORY_ITEM);
    private final ObservableList<InventoryItemTm> inventoryItemTmObservableList = FXCollections.observableArrayList();
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
    }

    public void setTableColumns() {
        colCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colMinOrderQty.setCellValueFactory(new PropertyValueFactory<>("minimumOrderQuantity"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colBuyingPrice.setCellValueFactory(new PropertyValueFactory<>("buyingPrice"));
        colSellingPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("updateBtn"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("deleteBtn"));
    }

    public void loadAllData(String searchText) throws SQLException, ClassNotFoundException {
        int count = 0;
        inventoryItemTmObservableList.clear();
        for (InventoryItemDto inventoryItemDto : (searchText.length() > 0) ? inventoryItemBo.searchInventoryItem(searchText) : inventoryItemBo.loadAllInventoryItems()) {
            // Create ImageViews for the buttons
            ImageView updateBtnImage = createImageView("com/moha/hms/assets/icons/editing.png");
            ImageView deleteBtnImage = createImageView("com/moha/hms/assets/icons/trash.png");

            Button updateBtn = createButton(updateBtnImage);
            Button deleteBtn = createButton(deleteBtnImage);
            count++;
            InventoryItemTm inventoryItemTm = new InventoryItemTm(
                    String.valueOf(count), inventoryItemDto.getId(), inventoryItemDto.getName(),
                    inventoryItemDto.getQrCode(), inventoryItemDto.getCategory(), inventoryItemDto.getQtyOnHand(),
                    inventoryItemDto.getMinimumOrderQuantity(), inventoryItemDto.getSupplierName(),
                    inventoryItemDto.getBuyingPrice(), inventoryItemDto.getSellingPrice(),
                    updateBtn, deleteBtn
            );
            inventoryItemTmObservableList.add(inventoryItemTm);

            deleteBtn.setOnAction(event -> handleDeleteAction(inventoryItemDto));

            updateBtn.setOnAction(event -> handleUpdateAction(inventoryItemDto));
        }
        tblItem.setItems(inventoryItemTmObservableList);

        String userRoleName = getUserRoleName();
        if (userRoleName.equals("NURSE")){
            colDelete.setVisible(false);
        }
    }

    public void setUiWithData(String location, BorderPane context, InventoryItemDto inventoryItemDto) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/" + location + ".fxml"));
        Parent root = loader.load();
        SuperBaseController controller = loader.getController();

        if (controller instanceof InventoryItemAddNewFormController) {
            InventoryItemAddNewFormController inventoryItemAddNewFormController = (InventoryItemAddNewFormController) controller;
            inventoryItemAddNewFormController.saveBtn.setText("Update");
            inventoryItemAddNewFormController.saveBtn.setOnAction(event -> {
                if (inventoryItemAddNewFormController.preventNullValueOfSaveInventoryItem()) {
                    try {
                        boolean item = inventoryItemBo.updateInventoryItem(new InventoryItemDto(
                                inventoryItemDto.getId(), inventoryItemAddNewFormController.txtItemName.getText(),
                                inventoryItemDto.getQrCode(), inventoryItemAddNewFormController.txtCategory.getText(),
                                Integer.parseInt(inventoryItemAddNewFormController.txtQtyOnHand.getText()),
                                Integer.parseInt(inventoryItemAddNewFormController.txtMinOrderQty.getText()),
                                inventoryItemDto.getSupplierName(),
                                Double.parseDouble(inventoryItemAddNewFormController.txtBuyPrice.getText()),
                                Double.parseDouble(inventoryItemAddNewFormController.txtSellPrice.getText())
                        ));

                        if (item) {
                            new Alert(Alert.AlertType.INFORMATION, "Item is Updated").show();
                            inventoryItemAddNewFormController.clear();
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
                    }
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try again").show();
                }
            });

            inventoryItemAddNewFormController.setInventoryItemData(
                    inventoryItemDto.getName(), inventoryItemDto.getSupplierName(),
                    inventoryItemDto.getCategory(), String.valueOf(inventoryItemDto.getMinimumOrderQuantity()),
                    String.valueOf(inventoryItemDto.getQtyOnHand()), String.valueOf(inventoryItemDto.getBuyingPrice()),
                    String.valueOf(inventoryItemDto.getSellingPrice()),inventoryItemDto.getQrCode()
            );
        }
        context.getChildren().clear();
        context.setCenter(root);
    }

    private void handleDeleteAction(InventoryItemDto inventoryItemDto) {
        try {
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Are you sure to DELETE this item ???", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get().equals(ButtonType.YES)) {
                if (inventoryItemBo.deleteInventoryItem(inventoryItemDto.getId())) {
                    new Alert(Alert.AlertType.INFORMATION, "Item deleted !!!").show();
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

    private void handleUpdateAction(InventoryItemDto inventoryItemDto) {
        try {
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Do you want to UPDATE this item ???", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get().equals(ButtonType.YES)) {
                setUiWithData("InventoryItemAddNewForm", context, inventoryItemDto);
            }
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
        }
    }

    public void addNewBtnOnAction(ActionEvent event) throws IOException {
        setUi("InventoryItemAddNewForm", context);
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("InventoryItemManagementForm", context);
    }
}
