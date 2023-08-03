/*
 * Copyright (C) 2022, 2023 - Tillitis AB
 * SPDX-License-Identifier: GPL-2.0-only
 */
package main;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.control.TextField;

import java.io.IOException;

public class USSPopup {

    @FXML
    private TextField textField;

    private String result = "";

    public byte[] show() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/Views/TextInputPopup.fxml"));
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);

        Scene scene = new Scene(loader.load(), 300, 250);
        popupStage.setScene(scene);
        popupStage.setTitle("USS Entry");
        popupStage.showAndWait();

        return result.getBytes();
    }

    @FXML
    private void handleSubmit() {
        result = textField.getText().trim().isEmpty() ? "" : textField.getText();
        Stage stage = (Stage) textField.getScene().getWindow();
        stage.close();
    }
}

