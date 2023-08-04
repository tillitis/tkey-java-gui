/*
 * Copyright (C) 2022, 2023 - Tillitis AB
 * SPDX-License-Identifier: GPL-2.0-only
 */
package Controllers;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.*;
import main.*;
import java.io.File;
import java.nio.file.Files;

/**
 * Controller for signing tools/actions.
 */
public class SignerController {
    private Boolean connected = Main.connected;
    @FXML
    private TextArea textBox;
    private String name = "";
    private boolean isLoaded = false;
    @FXML
    private Button button;
    private CommonController commonController;

    public SignerController() {
        commonController = Main.commonController;
    }

    /**
     * Connect to TKey
     */
    @FXML
    private void connectButton() {
        connected = commonController.commonConnect(textBox);
    }

    /**
     * Get Name
     */
    @FXML
    private void getAppNameButton() throws Exception {
        if(!name.equals("")){
            textBox.appendText("App name: " + name + "\n");
        }else if(isLoaded && connected){
            name = Signer.getAppNameVersion();
            textBox.appendText("App Name: " + name + "\n");
        }else if(!isLoaded || !connected){
            textBox.appendText("TKey connection/app not found! \n");
            System.out.println(isLoaded);
            System.out.println(connected);
        }
        else{
            textBox.appendText("Cannot get name. Please reset the TKey first! \n");
        }
    }

    /**
     * Load app (signer)
     */
    @FXML
    private void loadSignerButton(){
        Main.setFilePath("signer.bin");
        try{
            USSPopup popup = new USSPopup();
            byte[] result = popup.show();
            commonController.commonLoadApp(true,textBox,result);
            isLoaded = true;
        }catch(Exception e){
            isLoaded = false;
        }
    }

    /**
     * Get Public key
     */
    @FXML
    public void getPubKeyButton() throws Exception{
        if(connected && isLoaded){
            String pubkey = Signer.bytesToHex(Signer.getPubKey());
            textBox.appendText("Pub Key: " + pubkey + " \n");
        }
        else{
            textBox.appendText("Failed. Check if TKey is connected and app is loaded! \n");
        }
    }

    /**
     * Sign file/do sign (returns ed25519 sig).
     */
    @FXML
    public void signFileButton(){
        FileChooser fileChooser = new FileChooser();

        Window stage = button.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            textBox.appendText("File found. Touch TKey when it starts blinking green. \n");

            Task<byte[]> task = new Task<>() {
                @Override
                protected byte[] call() throws Exception {
                    if (file.length() > 4096) {
                        throw new Exception("File too large to sign.");
                    }

                    String filePath = file.getAbsolutePath();
                    byte[] fileBytes = Files.readAllBytes((new File(filePath)).toPath());

                    return Signer.sign(fileBytes);
                }
            };

            task.setOnSucceeded(event -> {
                byte[] sig = task.getValue();
                textBox.appendText("Signature over message by TKey (on stdout): " + " \n" + Signer.bytesToHex(sig) +  " \n");
            });

            task.setOnFailed(event -> {
                Throwable e = task.getException();
                textBox.appendText("Failed to sign the file. Error: " + e.getMessage() + "\n");
            });

            new Thread(task).start();
        }
    }
}
