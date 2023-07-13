/*
 * Copyright (C) 2022, 2023 - Tillitis AB
 * SPDX-License-Identifier: GPL-2.0-only
 */

import com.tillitis.TkeyClient;
import com.tillitis.UDI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.io.File;

public class Controller {

    @FXML
    private Button button3;
    @FXML
    private TextArea textBox;
    private String name = "";
    private Boolean hasFile = false;
    private Boolean connected = false;
    private Boolean locked = false;

    @FXML
    private void button1Clicked() throws Exception {
        if(connected && !TkeyClient.getHasCon()){
            System.out.println(TkeyClient.getHasCon());
            connected = false;
        }
        try{
            if(!connected){
                TkeyClient.connect();
                textBox.appendText("Connected to the TKey successfully!" + "\n");
                connected = true;
            }else{
                textBox.appendText("Already connected to the Tkey!" + "\n");
            }
        }catch (Exception e){
            textBox.appendText("Failed to connect!" + "\n");
            TkeyClient.close();
            connected = false;
            System.out.println(TkeyClient.getHasCon());
            TkeyClient.reconnect();
        }
    }

    /**
     * Name is cached after first retrieval, due to bug which occurs if name is retrieved from
     * TKey several times over.
     */
    @FXML
    private void button2Clicked() throws Exception {
        if(!name.equals("")){
                textBox.appendText("TKey name and version: " + name + "\n");
        }else if(!locked && connected){
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
        if(!locked && connected){
            UDI udi = TkeyClient.getUDI();
            textBox.appendText("TKey UDI: 0x0" + Integer.toHexString(udi.vendorID()) + "0" + Integer.toHexString(udi.udi()[0]) + "00000" + Integer.toHexString(udi.serial()) + "\n");
            textBox.appendText("Vendor ID: " + Integer.toHexString(udi.vendorID()) + " Product ID: " + udi.productID() + " Product Rev: " + udi.productRevision() + "\n");
            TkeyClient.clearIOFull();
        } else if(!connected){
            textBox.appendText("TKey connection not found! \n");
        }
        else{
            textBox.appendText("Cannot get UDI if app has been loaded to device. Please reset the TKey first! \n");
        }
    }

    @FXML
    private void button6Clicked() throws Exception {
        if(!hasFile){
            textBox.appendText("No file specified! \n");
        } else if (!connected) {
            textBox.appendText("TKey connection not found! \n");
        } else{
            Main.loadApp();
            textBox.appendText("App Loaded!" + "\n");
            locked = true;
        }
    }
}
