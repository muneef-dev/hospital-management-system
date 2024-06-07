package com.moha.hms.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.moha.hms.bo.BoFactory;
import com.moha.hms.bo.custom.InventoryItemBo;
import com.moha.hms.dto.InventoryItemDto;
import com.moha.hms.util.KeyGenerator;
import com.moha.hms.util.QrCodeGenerator;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import org.apache.commons.codec.binary.Base64;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

public class InventoryItemAddNewFormController extends SuperBaseController {
    public BorderPane context;
    public ImageView backBtnImg;
    public ImageView viewBtnImg;
    public ImageView saveBtnImg;
    public ImageView clearBtnImg;
    public TextField txtItemName;
    public TextField txtSupplierName;
    public TextField txtCategory;
    public TextField txtMinOrderQty;
    public TextField txtQtyOnHand;
    public TextField txtBuyPrice;
    public TextField txtSellPrice;
    public ImageView imgQrCode;
    @FXML
    public Button saveBtn;

    InventoryItemBo inventoryItemBo = BoFactory.getInstance().getBo(BoFactory.BoType.INVENTORY_ITEM);
    String qrCodeUniqueData = null;
    BufferedImage bufferedImage = null;

    public void initialize(){
        try {
            setQrCode();
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveBtnOnAction(ActionEvent event) throws SQLException, ClassNotFoundException, IOException, WriterException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        javax.imageio.ImageIO.write(bufferedImage,"png", byteArrayOutputStream);
        byte[] arr = byteArrayOutputStream.toByteArray();

        if (preventNullValueOfSaveInventoryItem()) {
            boolean item = inventoryItemBo.createInventoryItem(new InventoryItemDto(
                    KeyGenerator.generateId(), txtItemName.getText(), Base64.encodeBase64String(arr),
                    txtCategory.getText(), Integer.parseInt(txtQtyOnHand.getText()),
                    Integer.parseInt(txtMinOrderQty.getText()), txtSupplierName.getText(),
                    Double.parseDouble(txtBuyPrice.getText()), Double.parseDouble(txtSellPrice.getText())
            ));

            if (item) {
                setQrCode();
                new Alert(Alert.AlertType.INFORMATION, "Inventory Item is Saved").show();
                clear();
            }
        }
    }

    private void setQrCode() throws WriterException {
        qrCodeUniqueData = QrCodeGenerator.generate(25);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        bufferedImage = MatrixToImageWriter.toBufferedImage(qrCodeWriter.encode(qrCodeUniqueData, BarcodeFormat.QR_CODE,201,205));

        Image image = SwingFXUtils.toFXImage(bufferedImage,null);
        imgQrCode.setImage(image);
    }

    public boolean preventNullValueOfSaveInventoryItem() {
        if (txtItemName.getText().isEmpty() || txtSupplierName.getText().isEmpty() || txtCategory.getText().isEmpty() ||
                txtMinOrderQty.getText().isEmpty() || txtQtyOnHand.getText().isEmpty() || txtBuyPrice.getText().isEmpty() ||
                txtSellPrice.getText().isEmpty()) {

            new Alert(Alert.AlertType.INFORMATION, "Fill all fields").show();
            return false;
        }
        if (!isNumeric(txtMinOrderQty.getText()) || !isNumeric(txtQtyOnHand.getText()) ||
                !isNumeric(txtBuyPrice.getText()) || !isNumeric(txtSellPrice.getText())) {
            new Alert(Alert.AlertType.WARNING, "Enter valid numeric values").show();
            return false;
        }
        return true;
    }

    public void clearBtnOnAction(ActionEvent event) {
        clear();
    }

    public void clear() {
        txtItemName.clear();
        txtSupplierName.clear();
        txtCategory.clear();
        txtMinOrderQty.clear();
        txtQtyOnHand.clear();
        txtBuyPrice.clear();
        txtSellPrice.clear();
    }

    public void setInventoryItemData(String itemName, String supplierName, String category,
                            String minOrderQty, String qtyOnHand, String buyPrice,
                            String sellPrice, String qrCode) {
        txtItemName.setText(itemName);
        txtSupplierName.setText(supplierName);
        txtCategory.setText(category);
        txtMinOrderQty.setText(minOrderQty);
        txtQtyOnHand.setText(qtyOnHand);
        txtBuyPrice.setText(buyPrice);
        txtSellPrice.setText(sellPrice);
        byte[] data = Base64.decodeBase64(qrCode);
        imgQrCode.setImage(new Image(new ByteArrayInputStream(data)));
    }

    public void backBtnOnAction(ActionEvent event) throws IOException {
        setUi("InventoryItemManagementForm", context);
    }

    public void viewBtnOnAction(ActionEvent event) throws IOException {
        setUi("InventoryItemsViewAndManageAllForm", context);
    }
}
