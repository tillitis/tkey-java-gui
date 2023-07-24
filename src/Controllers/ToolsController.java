/*
 * Copyright (C) 2022, 2023 - Tillitis AB
 * SPDX-License-Identifier: GPL-2.0-only
 */

package Controllers;

import com.tillitis.TkeyClient;
import com.tillitis.UDI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import main.Main;

import java.io.File;

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
    private void button1Clicked() {
        connected = controller.commonConnect(textBox);
    }

    /**
     * Name is cached after first retrieval, due to bug which occurs if name is retrieved from
     * TKey several times over.
     */
    @FXML
    private void button2Clicked() throws Exception {
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

    @FXML
    private void button3Clicked() {
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

    @FXML
    private void button4Clicked() throws Exception {
        if(!controller.getLocked() && connected){
            UDI udi = TkeyClient.getUDI();
            textBox.appendText("TKey UDI: 0x0" + Integer.toHexString(udi.getVendorID()) + "0" + Integer.toHexString(udi.getUdi()[0]) + "00000" + Integer.toHexString(udi.getSerial()) + "\n");
            textBox.appendText("Vendor ID: " + Integer.toHexString(udi.getVendorID()) + " Product ID: " + udi.getProductID() + " Product Rev: " + udi.getProductRevision() + "\n");
            TkeyClient.clearIOFull();
        } else if(!connected){
            textBox.appendText("TKey connection not found! \n");
        }
        else{
            textBox.appendText("Cannot get UDI if app has been loaded to device. Please reset the TKey first! \n");
        }
    }
    @FXML
    private void button6Clicked(){
        controller.commonLoadApp(hasFile,textBox);
    }
}
