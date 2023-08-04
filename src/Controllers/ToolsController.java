/*
 * Copyright (C) 2022, 2023 - Tillitis AB
 * SPDX-License-Identifier: GPL-2.0-only
 */
package Controllers;
import com.tillitis.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.*;
import main.*;
import java.io.*;
import static java.lang.Integer.toHexString;

/**
 * Controller for general Tkey tools.
 */
public class ToolsController {

    @FXML
    private Button button3;
    @FXML
    private TextArea textBox;
    private String name = "";
    private Boolean hasFile = false;
    private Boolean connected = Main.connected;
    private CommonController controller;

    public ToolsController() {
        controller = Main.commonController;
    }

    @FXML
    private void connectButton() {
        connected = controller.commonConnect(textBox);
    }

    /**
     * Name is cached after first retrieval, due to bug which occurs if name is retrieved from
     * TKey several times over.
     */
    @FXML
    private void getNameButton() throws Exception {
        if(!name.equals("")){
                textBox.appendText("TKey name and version: " + name + "\n");
        }else if(!controller.getLocked() && connected){
            name = TkeyClient.getNameVersion();
            textBox.appendText("TKey name and version: " + name + "\n");
        }else if(!connected){
            textBox.appendText("TKey connection not found! \n");
        }
        else{
            textBox.appendText("Cannot get name if app has been loaded to device. Please reset the TKey first! \n");
        }
    }

    /**
     * Opens Windows file browser.
     */
    @FXML
    private void chooseFileButton() {
        FileChooser fileChooser = new FileChooser();

        Window stage = button3.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            String filePath = file.getAbsolutePath();
            Main.setFilePath(filePath);
            textBox.appendText("Click 'Load App' to load it to the TKey \n");
            hasFile = true;
        }
    }

    /**
     * Gets TKey UDI
     */
    @FXML
    private void getUDIButton() throws Exception {
        if(!controller.getLocked() && connected){
            UDI udi = TkeyClient.getUDI();
            textBox.appendText("TKey UDI: 0x0" + toHexString(udi.getVendorID()) + "0" + toHexString(udi.getUdi()[0]) + "00000" + toHexString(udi.getSerial()) + "\n");
            textBox.appendText("Vendor ID: " + toHexString(udi.getVendorID()) + " Product ID: " + udi.getProductID() + " Product Rev: " + udi.getProductRevision() + "\n");
            TkeyClient.clearIOFull();
        } else if(!connected){
            textBox.appendText("TKey connection not found! \n");
        }
        else{
            textBox.appendText("Cannot get UDI if app has been loaded to device. Please reset the TKey first! \n");
        }
    }

    /**
     * Loads retrieved (chooseFileButton action) app, and gets optional USS.
     */
    @FXML
    private void loadAppButton() throws IOException {
        USSPopup popup = new USSPopup();
        byte[] result = popup.show();
        controller.commonLoadApp(hasFile,textBox,result);
    }
}
