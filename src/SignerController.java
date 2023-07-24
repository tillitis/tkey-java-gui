import com.tillitis.TkeyClient;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
/*
 * Copyright (C) 2022, 2023 - Tillitis AB
 * SPDX-License-Identifier: GPL-2.0-only
 */
public class SignerController {

    private Boolean connected = false;
    @FXML
    private TextArea textBox;
    private String name = "";
    private boolean isLoaded = false;

    @FXML
    private Button button;

    /**
     * Connect to TKey
     */
    @FXML
    private void button1Clicked() {
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
        }
    }

    /**
     * Get Name
     */
    @FXML
    private void button2Clicked() throws Exception {
        if(!name.equals("")){
            textBox.appendText("App name: " + name + "\n");
        }else if(isLoaded && connected){
            name = Signer.getAppNameVersion();
            textBox.appendText("App Name: " + name + "\n");
        }else if(!isLoaded || !connected){
            textBox.appendText("TKey connection/app not found! \n");
        }
        else{
            textBox.appendText("Cannot get name. Please reset the TKey first! \n");
        }
    }

    /**
     * Load app (signer)
     */
    @FXML
    private void button3Clicked() throws Exception {
        if (!connected) {
            textBox.appendText("TKey connection not found! \n");
        } else{
            Main.setFilePath("signer.bin");
            Main.loadApp();
            textBox.appendText("App Loaded! \n");
            isLoaded = true;
        }
    }

    /**
     * Get Public key
     * @throws Exception
     */
    @FXML
    public void button4Clicked() throws Exception{
        if(connected && isLoaded){
            String pubkey = Signer.bytesToHex(Signer.getPubKey());
            textBox.appendText("Pub Key: " + pubkey + "\n");
        }
        else{
            textBox.appendText("Failed. Check if TKey is connected and app is loaded!");
        }
    }

    /**
     * Sign file (returns ed25519 sig).
     */
    @FXML
    public void button5Clicked() throws Exception {
        FileChooser fileChooser = new FileChooser();

        Window stage = button.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            String filePath = file.getAbsolutePath();
            byte[] fileBytes = Files.readAllBytes((new File(filePath)).toPath());
            byte[] sig = Signer.sign(fileBytes);
            textBox.appendText("Click 'Load App' to load it to the TKey \n");

        }
    }
}
